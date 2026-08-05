package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = CyanAccent,
    onPrimary = Color(0xFF381E72),
    primaryContainer = Color(0xFF4A4458),
    onPrimaryContainer = SlateTextPrimary,
    secondary = EmeraldRootGreen,
    onSecondary = TechSapphireBg,
    secondaryContainer = EmeraldRootBg,
    onSecondaryContainer = SlateTextPrimary,
    tertiary = AmberWarning,
    background = TechSapphireBg,
    onBackground = SlateTextPrimary,
    surface = TechCardBg,
    onSurface = SlateTextPrimary,
    surfaceVariant = TechCardBorder,
    onSurfaceVariant = SlateTextSecondary,
    error = RoseError,
    onError = SlateTextPrimary
)

@Composable
fun DroidVmTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
