package com.example.client__android_app.ui.components
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun Instructions() {
    val instructions = """
        Instructions:
        1. Start InputRedirection on your DS via Rosalina menu (Press L+⬇+Select).
        2. Connect your Gamepad to your device.
        3. Enter the IP address of your 3DS.
        ℹ Work via Wi-Fi or via Mobile Data if both devices share the same network.
    """.trimIndent()
    Text(instructions)
}