// InputRedirector.kt

package com.example.client__android_app.data.network

import android.view.KeyEvent
import kotlin.math.abs

class InputRedirector(private val udpSender: UdpSender) {

    // Buttons state (all released by default)
    private var hidPad = 0xFFF

    // Analog stick values
    private var circleX = 0
    private var circleY = 0

    private val DEADZONE = 0.2f

    /** Handle gamepad button press */
    fun onKeyDown(keyCode: Int): Boolean {
        BUTTON_MAP[keyCode]?.let { bit ->
            hidPad = hidPad and (1 shl bit).inv()
            sendFrame()
            return true
        }
        return false
    }

    /** Handle gamepad button release */
    fun onKeyUp(keyCode: Int): Boolean {
        BUTTON_MAP[keyCode]?.let { bit ->
            hidPad = hidPad or (1 shl bit)
            sendFrame()
            return true
        }
        return false
    }

    /** Handle joystick / analog stick movement */
    fun onMotion(lx: Float, ly: Float, rx: Float = 0f, ry: Float = 0f) {
        fun applyDeadzone(v: Float) = if (abs(v) < DEADZONE) 0f else v

        // Scale to 16-bit signed int, invert Y like Qt
        circleX = (applyDeadzone(lx) * 32767f).toInt()
        circleY = (-applyDeadzone(ly) * 32767f).toInt()

        sendFrame()
    }

    /** Send the current state over UDP */
    private fun sendFrame() {
        udpSender.sendFrame(hidPad, circleX, circleY)
    }

    companion object {
        /** Maps Android key codes to 3DS button bits */
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