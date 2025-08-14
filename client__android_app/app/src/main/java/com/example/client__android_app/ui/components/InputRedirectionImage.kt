package com.example.client__android_app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.client__android_app.R

@Composable
fun InputRedirectionImage() {
    Column (
        Modifier
            .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(id = R.drawable.input_redirection_image),
            contentDescription = "InputRedirection Logo",
            modifier = Modifier
                .size(100.dp)
        )
    }
}