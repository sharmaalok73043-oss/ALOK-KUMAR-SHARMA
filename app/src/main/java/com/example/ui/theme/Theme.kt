package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

import androidx.compose.runtime.CompositionLocalProvider

private val LightColorScheme = lightColorScheme(
    primary = GeoAccentDeep,
    onPrimary = Color.White,
    secondary = GeoBeigeMuted,
    onSecondary = GeoTextPrimary,
    background = GeoBackground,
    onBackground = GeoTextPrimary,
    surface = Color.White,
    onSurface = GeoTextPrimary,
    surfaceVariant = GeoLightCream,
    onSurfaceVariant = GeoTextSecondary
)

private val DarkColorScheme = darkColorScheme(
    primary = GeoBeigeMuted,
    onPrimary = GeoTextPrimary,
    secondary = GeoAccentDeep,
    onSecondary = Color.White,
    background = Color(0xFF1E1C1A),
    onBackground = Color(0xFFE6E1DE),
    surface = Color(0xFF2B2826),
    onSurface = Color(0xFFE6E1DE),
    surfaceVariant = Color(0xFF3B3734),
    onSurfaceVariant = Color(0xFFCAC5C2)
)

@Composable
fun MyApplicationTheme(
    themeIndex: Int = 0,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val appColors = when (themeIndex) {
        1 -> CosmicNightColors
        2 -> TealOceanColors
        3 -> ForestMossColors
        else -> ClassicWarmColors
    }

    val systemColors = if (themeIndex == 1) {
        DarkColorScheme.copy(
            primary = appColors.primaryBrown,
            secondary = appColors.pastelPeach,
            background = appColors.backgroundBeige,
            surface = appColors.softCreamSurface,
            onBackground = appColors.textDeepPrivate,
            onSurface = appColors.textDeepPrivate
        )
    } else {
        LightColorScheme.copy(
            primary = appColors.primaryBrown,
            secondary = appColors.pastelPeach,
            background = appColors.backgroundBeige,
            surface = appColors.softCreamSurface,
            onBackground = appColors.textDeepPrivate,
            onSurface = appColors.textDeepPrivate
        )
    }

    CompositionLocalProvider(LocalAppColors provides appColors) {
        MaterialTheme(
            colorScheme = systemColors,
            typography = Typography,
            content = content
        )
    }
}
