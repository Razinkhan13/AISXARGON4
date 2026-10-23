package com.example.aiscockpit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = StitchCyan,
    onPrimary = ObsidianBg,
    primaryContainer = ObsidianSurface2,
    onPrimaryContainer = StitchCyan,
    secondary = StitchEmerald,
    onSecondary = ObsidianBg,
    secondaryContainer = ObsidianSurface2,
    onSecondaryContainer = StitchEmerald,
    tertiary = NeonRose,
    onTertiary = SlateTextPrimary,
    background = ObsidianBg,
    onBackground = SlateTextPrimary,
    surface = ObsidianSurface1,
    onSurface = SlateTextPrimary,
    surfaceVariant = ObsidianSurface2,
    onSurfaceVariant = SlateTextSecondary,
    outline = SlateBorder
)

@Composable
fun AISCockpitTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
