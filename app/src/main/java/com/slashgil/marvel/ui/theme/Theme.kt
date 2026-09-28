package com.slashgil.marvel.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val MarvelDarkColorScheme = darkColorScheme(
    primary = MarvelRed,
    onPrimary = TextMain,
    primaryContainer = MarvelRedDark,
    background = BgMain,
    onBackground = TextMain,
    surface = BgSurface,
    onSurface = TextMain,
    surfaceVariant = BgSurface,
    onSurfaceVariant = TextSecondary
)

@Composable
fun MarvelTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MarvelDarkColorScheme,
        typography = Typography,
        content = content
    )
}
