package com.example.client__android_app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun IpAddressInput(
    ipAddress: String,
    onIpAddressChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = ipAddress,
        onValueChange = onIpAddressChanged,
        label = { Text("IP Address \uD83D\uDCF6") },
        placeholder = { Text("192.168.x.xx") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        )
    )
}