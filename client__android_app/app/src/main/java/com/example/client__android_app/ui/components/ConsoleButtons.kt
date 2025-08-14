package com.example.client__android_app.ui.components
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DSConsoleButtons() {
    Column (
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Button(
            onClick = {
                Log.d("sireoh", "Home Button Clicked")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Home \uD83C\uDFE0")
        }
        Button(
            onClick = {
                Log.d("sireoh", "Power Button Clicked")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Power \uD83D\uDD0C")
        }
        Button(
            onClick = {
                Log.d("sireoh", "Power-Long Button Clicked")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Power-Long \uD83D\uDD0C\uD83D\uDD0C\uD83D\uDD0C")
        }
        Button(
            onClick = {
                Log.d("sireoh", "Settings Button Clicked")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Settings ⚙\uFE0F")
        }
    }
}