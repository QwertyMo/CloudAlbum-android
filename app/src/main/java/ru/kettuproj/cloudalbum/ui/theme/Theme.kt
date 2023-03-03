package ru.kettuproj.cloudalbum.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Color(103, 58, 183, 255),
    primaryVariant = Color(80, 54, 126, 255),
    secondary = Color(255, 255, 255, 255),
    background = Color(41, 41, 41, 255),
    surface = Color(41, 41, 41, 255),
    onPrimary = Color(41, 41, 41, 255),
    onSecondary = Color(143, 16, 16, 255),
    onBackground = Color(255, 255, 255, 255),
    onSurface = Color(255, 255, 255, 255),
)

private val LightColorPalette = lightColors(
    primary = Color(103, 58, 183, 255),
    secondary = Color(60, 27, 27, 255),
    background = Color(255, 255, 255, 255),
    surface = Color(242, 235, 255, 255),
    onPrimary = Color(255, 255, 255, 255),
    onSecondary = Color(143, 16, 16, 255),
    onBackground = Color(0, 0, 0, 255),
    onSurface = Color(36, 34, 39, 255),
)

@Composable
fun CloudAlbumTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}