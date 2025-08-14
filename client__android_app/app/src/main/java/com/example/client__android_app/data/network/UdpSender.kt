// UdpSender.kt

package com.example.client__android_app.data.network

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.nio.ByteBuffer
import java.nio.ByteOrder

class UdpSender(private var ip: String, private val port: Int = 4950) {
    private val socket = DatagramSocket()
    private val scope = CoroutineScope(Dispatchers.IO)

    fun updateIp(newIp: String) {
        ip = newIp
    }

    fun sendFrame(hidPad: Int) {
        scope.launch {
            try {
                val buffer = ByteBuffer.allocate(16).order(ByteOrder.LITTLE_ENDIAN)
                buffer.putInt(hidPad)
                buffer.putInt(0) // Circle pad (unused)
                buffer.putInt(0) // CPP (unused)
                buffer.putInt(0) // Interface buttons (unused)

                val data = buffer.array()
                val packet = DatagramPacket(data, data.size, InetAddress.getByName(ip), port)
                socket.send(packet)
            } catch (e: Exception) {
                Log.e("sireoh", "UdpSender - Failed to send to $ip:$port", e)
            }
        }
    }
}