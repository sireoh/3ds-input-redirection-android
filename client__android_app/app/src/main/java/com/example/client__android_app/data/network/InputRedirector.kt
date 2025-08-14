// InputRedirector.kt

package com.example.client__android_app.data.network

import android.view.KeyEvent
import kotlin.math.sqrt

class InputRedirector(private val udpSender: UdpSender) {
    private var hidPad = 0xFFF // All buttons released state
    private var lx = 0.0
    private var ly = 0.0
    private var rx = 0.0
    private var ry = 0.0
    private var interfaceButtons = 0

    // Constants from reference code
    private val CPAD_BOUND = 0x5d0
    private val CPP_BOUND = 0x7f

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

    fun setAxisValues(leftX: Double, leftY: Double, rightX: Double, rightY: Double) {
        lx = leftX
        ly = leftY
        rx = rightX
        ry = rightY
        sendFrame()
    }

    fun setInterfaceButton(button: InterfaceButton, pressed: Boolean) {
        interfaceButtons = if (pressed) {
            interfaceButtons or button.mask
        } else {
            interfaceButtons and button.mask.inv()
        }
        sendFrame()
    }

    private fun sendFrame() {
        var circlePadState = 0x7ff7ff
        var cppState = 0x80800081.toInt()

        // Calculate circle pad state
        if (lx != 0.0 || ly != 0.0) {
            var x = (lx * CPAD_BOUND + 0x800).toInt()
            var y = (ly * CPAD_BOUND + 0x800).toInt()
            x = if (x >= 0xfff) if (lx < 0.0) 0x000 else 0xfff else x
            y = if (y >= 0xfff) if (ly < 0.0) 0x000 else 0xfff else y
            circlePadState = (y shl 12) or x
        }

        // Calculate cpp state
        if (rx != 0.0 || ry != 0.0) {
            val sqrtHalf = sqrt(0.5)
            var x = (sqrtHalf * (rx + ry) * CPP_BOUND + 0x80).toInt()
            var y = (sqrtHalf * (ry - rx) * CPP_BOUND + 0x80).toInt()
            x = if (x >= 0xff) if (rx < 0.0) 0x00 else 0xff else x
            y = if (y >= 0xff) if (ry < 0.0) 0x00 else 0xff else y
            cppState = (y shl 24) or (x shl 16) or 0x81
        }

        udpSender.sendFrame(
            hidPad = hidPad,
            circlePadState = circlePadState,
            cppState = cppState,
            interfaceButtons = interfaceButtons
        )
    }

    enum class InterfaceButton(val mask: Int) {
        HOME(1),
        POWER(2),
        POWER_LONG(4)
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