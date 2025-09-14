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

// 🍑 Peach Palette kommt aus Color.kt:
// Peach, PeachDeep, SoftRose, Vanilla, Mint, CocoaBrown, Charcoal

private val DarkColorScheme = darkColorScheme(
    primary = PeachDeep,
    onPrimary = Charcoal,
    primaryContainer = CocoaBrown,
    onPrimaryContainer = Vanilla,

    secondary = Mint,
    onSecondary = Charcoal,
    secondaryContainer = CocoaBrown,
    onSecondaryContainer = Vanilla,

    tertiary = SoftRose,
    onTertiary = Charcoal,

    background = Charcoal,
    onBackground = Vanilla,

    surface = Charcoal,
    onSurface = Vanilla,

    error = SoftRose,
    onError = Charcoal
)

private val LightColorScheme = lightColorScheme(
    primary = Peach,                 // Hauptakzent
    onPrimary = Charcoal,
    primaryContainer = PeachDeep,    // etwas kräftigerer Peach
    onPrimaryContainer = Charcoal,

    secondary = SoftRose,            // softer rosé Zweitakzent
    onSecondary = Charcoal,
    secondaryContainer = Vanilla,
    onSecondaryContainer = CocoaBrown,

    tertiary = Mint,                 // frischer Kontrast
    onTertiary = Charcoal,

    background = Vanilla,            // cremiger Grund
    onBackground = Charcoal,

    surface = Vanilla,
    onSurface = Charcoal,

    error = SoftRose,
    onError = Charcoal
)

@Composable
fun LumakaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // auf false lassen, damit Peach nicht von Material You überschrieben wird
    dynamicColor: Boolean = false,
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
