package com.example.client__android_app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle

@Composable
fun Credits() {
    val uriHandler = LocalUriHandler.current
    val credits = buildAnnotatedString {
        append("Created by ")
        pushStringAnnotation(
            tag = "URL",
            annotation = "https://github.com/sireoh/"
        )
        withStyle(style = SpanStyle(
            color = Color.Cyan,
            textDecoration = TextDecoration.Underline
        )) {
            append("sireoh")
        }
        pop()
        append("\nInspired from InputRedirectionClient-Qt by ")
        pushStringAnnotation(
            tag = "URL",
            annotation = "https://github.com/TuxSH/"
        )
        withStyle(style = SpanStyle(
            color = Color.Cyan,
            textDecoration = TextDecoration.Underline
        )) {
            append("TuxSH")
        }
        pop()
    }

    Text(
        text = credits,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null  // Removes the ripple effect
            ) {
                uriHandler.openUri("https://github.com/sireoh/")
            },
        textAlign = TextAlign.Center
    )
}