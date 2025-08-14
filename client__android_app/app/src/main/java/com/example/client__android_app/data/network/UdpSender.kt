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
    private var socket: DatagramSocket = DatagramSocket()
    private val scope = CoroutineScope(Dispatchers.IO)

    fun updateIp(newIp: String) {
        if (newIp == ip) return // no change, do nothing

        ip = newIp
        // Recreate the socket to avoid sending to old IP
        recreateSocket()
    }

    private fun recreateSocket() {
        try {
            if (!socket.isClosed) socket.close()
        } catch (e: Exception) {
            Log.e("sireoh", "Error closing old socket", e)
        }
        socket = DatagramSocket()
        Log.d("sireoh", "UdpSender socket reopened for IP: $ip")
    }

    fun sendFrame(hidPad: Int, circleX: Int = 0, circleY: Int = 0) {
        scope.launch {
            try {
                val buffer = ByteBuffer.allocate(16).order(ByteOrder.LITTLE_ENDIAN)
                buffer.putInt(hidPad)
                buffer.putInt(circleX)
                buffer.putInt(circleY)
                buffer.putInt(0) // Interface buttons (unused)

                val data = buffer.array()
                val packet = DatagramPacket(data, data.size, InetAddress.getByName(ip), port)
                socket.send(packet)
            } catch (e: Exception) {
                Log.e("sireoh", "UdpSender - Failed to send to $ip:$port", e)
            }
        }
    }

    fun close() {
        try {
            if (!socket.isClosed) {
                socket.close()
                Log.d("sireoh", "UdpSender socket closed")
            }
        } catch (e: Exception) {
            Log.e("sireoh", "Error closing socket", e)
        }
    }
}