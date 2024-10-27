package com.example.cloudmate.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = NavyBlue,
    onPrimary = White,
    primaryContainer = LightNavyBlue,
    secondary = Blue,
    onSecondary = White,
    secondaryContainer = SecondaryVariantDark,
    tertiary = LightNavyBlue,
    background = DarkBackground,
    onBackground = OnBackgroundDark,
    surface = DarkSurface,
    onSurface = OnSurfaceDark,
    error = ErrorDark,
    onError = White
)

private val LightColorScheme = lightColorScheme(
    primary = NavyBlue,
    onPrimary = White,
    primaryContainer = LightPrimaryContainer,
    secondary = Blue,
    onSecondary = LightNavyBlue,
    secondaryContainer = LightSecondaryContainer,
    tertiary = TertiaryLight,
    background = LightBackground,
    onBackground = OnBackgroundLight,
    surface = LightSurface,
    onSurface = OnSurfaceLight,
    error = ErrorLight,
    onError = White
)

@Composable
fun CloudMateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) DarkColorScheme else LightColorScheme
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
        shapes = Shape
    )
}