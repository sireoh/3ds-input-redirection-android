// InputRedirector.kt

package com.example.client__android_app.data.network

import android.view.KeyEvent

class InputRedirector(private val udpSender: UdpSender) {
    private var hidPad = 0xFFF // Default all buttons released

    fun onKeyDown(keyCode: Int): Boolean {
        BUTTON_MAP[keyCode]?.let { bit ->
            hidPad = hidPad and (1 shl bit).inv()
            sendFrame()
            return true
        }
        return false
    }

    fun onKeyUp(keyCode: Int): Boolean {
        BUTTON_MAP[keyCode]?.let { bit ->
            hidPad = hidPad or (1 shl bit)
            sendFrame()
            return true
        }
        return false
    }

    private fun sendFrame() {
        udpSender.sendFrame(hidPad = hidPad)
    }

    companion object {
        val BUTTON_MAP = mapOf(
            KeyEvent.KEYCODE_BUTTON_A to 0,
            KeyEvent.KEYCODE_BUTTON_B to 1,
            KeyEvent.KEYCODE_BUTTON_SELECT to 2,
            KeyEvent.KEYCODE_BUTTON_START to 3,
            KeyEvent.KEYCODE_DPAD_RIGHT to 4,
            KeyEvent.KEYCODE_DPAD_LEFT to 5,
            KeyEvent.KEYCODE_DPAD_UP to 6,
            KeyEvent.KEYCODE_DPAD_DOWN to 7,
            KeyEvent.KEYCODE_BUTTON_R1 to 8,
            KeyEvent.KEYCODE_BUTTON_L1 to 9,
            KeyEvent.KEYCODE_BUTTON_X to 10,
            KeyEvent.KEYCODE_BUTTON_Y to 11
        )
    }
}