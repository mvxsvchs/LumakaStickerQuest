package com.example.lumaka.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// ForestGreen, WarmBrown, Cream, Terracotta

private val DarkColorScheme = darkColorScheme(
    primary = ForestGreen,
    onPrimary = Cream,
    primaryContainer = WarmBrown,
    onPrimaryContainer = Cream,

    secondary = WarmBrown,
    onSecondary = Cream,
    secondaryContainer = ForestGreen,
    onSecondaryContainer = Cream,

    tertiary = Cream,
    onTertiary = ForestGreen,

    background = WarmBrown,   // dunkles, warmes Braun
    onBackground = Cream,     // warmer Hellton für Text

    surface = WarmBrown,
    onSurface = Cream,

    error = Terracotta,
    onError = Cream
)

private val LightColorScheme = lightColorScheme(
    primary = Terracotta,         // warmes Terracotta als Akzent
    onPrimary = Cream,
    primaryContainer = WarmBrown,
    onPrimaryContainer = Cream,

    secondary = ForestGreen,      // erdiges Grün als Gegenakzent
    onSecondary = Cream,
    secondaryContainer = ForestGreen,
    onSecondaryContainer = Cream,

    tertiary = WarmBrown,
    onTertiary = Cream,

    background = Cream,           // cremiger Grund
    onBackground = WarmBrown,

    surface = Cream,
    onSurface = WarmBrown,

    error = Terracotta,
    onError = Cream
)

@Composable
fun LumakaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Stell auf false, wenn du IMMER die cozy Palette willst
    dynamicColor: Boolean = true,
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
