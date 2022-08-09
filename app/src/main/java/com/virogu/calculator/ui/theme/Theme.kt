package com.virogu.calculator.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColors(
    primary = Purple80,
    secondary = PurpleGrey80,
    background = Color(0xFF010001),
    surface = Color(0xFF010001),
    onPrimary = Color(0xFF010001),
    onSecondary = Color(0xFF010001),
    onBackground = Color(0xFFE9E7E9),
    onSurface = Color(0xFFE9E7E9),
)

private val LightColorScheme = lightColors(
    primary = Purple40,
    secondary = PurpleGrey40,
    background = Color(0xFFffffff),
    surface = Color(0xFFffffff),
    onPrimary = Color(0xFFffffff),
    onSecondary = Color(0xFFffffff),
    onBackground = Color(0xFF010001),
    onSurface = Color(0xFF010001),
)

@Composable
fun CalculatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) DarkColorScheme else LightColorScheme
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.apply {
                statusBarColor = colorScheme.surface.toArgb()
                WindowCompat.getInsetsController(
                    this,
                    view
                ).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colors = colorScheme,
        typography = Typography,
        content = content
    )
}
