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

// 🌸 Light Cozy Pastell
private val LightColorScheme = lightColorScheme(
    primary = Peach,
    onPrimary = TextDark,
    primaryContainer = SoftPink,
    onPrimaryContainer = TextDark,

    secondary = Lavender,
    onSecondary = TextDark,
    secondaryContainer = PeachWhite,
    onSecondaryContainer = TextDark,

    tertiary = SoftPink,
    onTertiary = TextDark,

    background = PeachWhite,
    onBackground = TextDark,

    surface = PeachWhite,
    onSurface = TextDark,

    error = SoftPink,
    onError = TextDark
)

// 🌌 Dark Cozy Pastell
private val DarkColorScheme = darkColorScheme(
    primary = LavenderAccent,
    onPrimary = TextLight,
    primaryContainer = PeachAccent,
    onPrimaryContainer = TextLight,

    secondary = Lavender,
    onSecondary = TextLight,
    secondaryContainer = DarkSurface,
    onSecondaryContainer = TextLight,

    tertiary = PeachAccent,
    onTertiary = TextLight,

    background = DarkBg,
    onBackground = TextLight,

    surface = DarkSurface,
    onSurface = TextLight,

    error = PeachAccent,
    onError = TextLight
)

@Composable
fun LumakaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // pastel Palette erzwingen
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
