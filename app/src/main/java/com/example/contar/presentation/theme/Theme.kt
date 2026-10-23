package com.example.contar.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material3.MaterialTheme

@Composable
fun ContarTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme.copy(
        primary = Color(0xFF00E5FF),
        onPrimary = Color(0xFF00363A),
        primaryContainer = Color(0xFF004D53),
        onPrimaryContainer = Color(0xFF80F3FF),
        secondary = Color(0xFF69F0AE),
        onSecondary = Color(0xFF003919),
        secondaryContainer = Color(0xFF1B2E24),
        onSecondaryContainer = Color(0xFFA7F7CA),
        background = Color(0xFF000000),
        onBackground = Color(0xFFFFFFFF),
        onSurface = Color(0xFFFFFFFF),
        onSurfaceVariant = Color(0xFF90A4AE),
        outline = Color(0xFF37474F)
    )

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}