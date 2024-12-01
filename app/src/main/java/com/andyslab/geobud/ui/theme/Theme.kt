package com.andyslab.geobud.ui.theme

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
    /*primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80*/
)

private val LightColorScheme = lightColorScheme(
    primary = blue_primary,
    onPrimary = Color.White,
    primaryContainer = Color.White,
    onPrimaryContainer = Color.Black,
    inversePrimary = blue_primary_transparent,

    secondary = green_primary,
    onSecondary = Color.White,
    secondaryContainer = Color.White,
    onSecondaryContainer = Color.Black,

    tertiary = gray,
    tertiaryContainer = black_transparent1,
    onTertiaryContainer = Color.White,

    background = Color.White,
    onBackground = Color.Black,

    surface = Color.White,
    onSurface = Color.Black,
    surfaceTint = blue_primary_glow,

    surfaceVariant = green_primary_dark,
    onSurfaceVariant = green_primary_medium,

    surfaceContainerLow = blue_extra_light,

    outline = Color.Black,
    outlineVariant = Color.Yellow,

    scrim = black_transparent2,
    error = red_error
)

@Composable
fun GeobudTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.scrim.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}