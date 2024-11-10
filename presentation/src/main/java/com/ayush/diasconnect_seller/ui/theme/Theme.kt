package com.ayush.diasconnect_seller.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Blue,
    onPrimary = Color.White,
    primaryContainer = BlueLight,
    onPrimaryContainer = Gray900,

    secondary = Gray600,
    onSecondary = Color.White,
    secondaryContainer = Gray200,
    onSecondaryContainer = Gray800,

    tertiary = Warning,
    onTertiary = Color.White,
    tertiaryContainer = WarningLight,
    onTertiaryContainer = Gray900,

    error = Error,
    onError = Color.White,
    errorContainer = ErrorLight,
    onErrorContainer = Gray900,

    background = LightBackground,
    onBackground = Gray900,
    surface = LightSurface,
    onSurface = Gray900,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = Gray700,

    outline = Gray400,
    outlineVariant = Gray300,

    scrim = Gray900.copy(alpha = 0.32f)
)

private val DarkColorScheme = darkColorScheme(
    primary = BlueLight,
    onPrimary = Gray900,
    primaryContainer = Blue,
    onPrimaryContainer = Color.White,

    secondary = Gray400,
    onSecondary = Gray900,
    secondaryContainer = Gray700,
    onSecondaryContainer = Color.White,

    tertiary = WarningLight,
    onTertiary = Gray900,
    tertiaryContainer = Warning,
    onTertiaryContainer = Color.White,

    error = ErrorLight,
    onError = Gray900,
    errorContainer = Error,
    onErrorContainer = Color.White,

    background = DarkBackground,
    onBackground = Gray100,
    surface = DarkSurface,
    onSurface = Gray100,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Gray300,

    outline = Gray600,
    outlineVariant = Gray700,

    scrim = Color.Black.copy(alpha = 0.32f)
)

@Composable
fun DiasConnectSellerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set to false by default to maintain brand consistency
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}

// Extension functions for common color operations
fun Color.withAlpha(alpha: Float): Color = copy(alpha = alpha)

@Composable
fun surfaceColorAtElevation(elevation: Float): Color {
    return if (isSystemInDarkTheme()) {
        MaterialTheme.colorScheme.surface.copy(alpha = 0.9f + (elevation * 0.01f))
    } else {
        MaterialTheme.colorScheme.surface.copy(alpha = 1f - (elevation * 0.01f))
    }
}