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

    fun sendFrame(
        hidPad: Int,
        circlePadState: Int = 0x7ff7ff,
        cppState: Int = 0x80800081.toInt(),
        interfaceButtons: Int = 0
    ) {
        scope.launch {
            try {
                Log.d("sireoh", "UdpSender - Preparing to send to $ip:$port")

                val buffer = ByteBuffer.allocate(16).order(ByteOrder.LITTLE_ENDIAN)
                buffer.putInt(hidPad)
                buffer.putInt(circlePadState)
                buffer.putInt(cppState)
                buffer.putInt(interfaceButtons)

                val data = buffer.array()
                val packet = DatagramPacket(data, data.size, InetAddress.getByName(ip), port)
                socket.send(packet)

                Log.d("sireoh", "UdpSender - Sent frame - " +
                        "HID - ${hidPad.toString(16)}, " +
                        "CirclePad - ${circlePadState.toString(16)}, " +
                        "CPP - ${cppState.toString(16)}, " +
                        "Interface - ${interfaceButtons.toString(16)}")
                Log.d("sireoh", "UdpSender - Frame sent successfully to $ip")
            } catch (e: Exception) {
                Log.e("sireoh", "UdpSender - Failed to send to $ip:$port", e)
            }
        }
    }
}