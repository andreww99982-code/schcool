package com.schcool.trainer.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Yellow-white palette
val YellowPrimary = Color(0xFFF5C518)       // bright golden-yellow
val YellowDark = Color(0xFFD4A017)          // deep amber for pressed / dark accent
val YellowContainer = Color(0xFFFFF3B0)     // very light yellow for containers
val OnYellow = Color(0xFF1A1A00)            // near-black for text on yellow
val BackgroundCream = Color(0xFFFFFDE7)     // warm off-white background
val SurfaceWhite = Color(0xFFFFFFFF)
val ErrorRed = Color(0xFFB00020)
val GoodGreen = Color(0xFF2E7D32)

private val YellowWhiteColorScheme = lightColorScheme(
    primary = YellowPrimary,
    onPrimary = OnYellow,
    primaryContainer = YellowContainer,
    onPrimaryContainer = OnYellow,
    secondary = YellowDark,
    onSecondary = OnYellow,
    secondaryContainer = Color(0xFFFFECB3),
    onSecondaryContainer = OnYellow,
    background = BackgroundCream,
    onBackground = Color(0xFF1A1A00),
    surface = SurfaceWhite,
    onSurface = Color(0xFF1A1A00),
    surfaceVariant = Color(0xFFFFF9C4),
    onSurfaceVariant = Color(0xFF3A3A00),
    error = ErrorRed,
    onError = Color.White,
    outline = YellowDark
)

@Composable
fun SchcoolTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = YellowWhiteColorScheme,
        typography = Typography,
        content = content
    )
}
