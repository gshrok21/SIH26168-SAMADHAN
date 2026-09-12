package com.navdr.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    secondary = AccentCyan,
    onSecondary = DeepNavy,
    tertiary = StatusSuccessEmerald,
    background = DeepNavy,
    onBackground = PrimaryTextDarkTheme,
    surface = DarkCardBg,
    onSurface = PrimaryTextDarkTheme,
    surfaceVariant = CardBorderColor,
    onSurfaceVariant = SecondaryTextDarkTheme,
    error = StatusDangerRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    secondary = AccentCyan,
    onSecondary = Color.White,
    tertiary = StatusSuccessEmerald,
    background = LightBackground,
    onBackground = PrimaryTextLightTheme,
    surface = LightCardBg,
    onSurface = PrimaryTextLightTheme,
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = SecondaryTextLightTheme,
    error = StatusDangerRed,
    onError = Color.White
)

@Composable
fun NavDRTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = NavDrTypography,
        shapes = NavDrShapes,
        content = content
    )
}
