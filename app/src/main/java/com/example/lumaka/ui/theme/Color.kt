package com.example.lumaka.ui.theme

import androidx.compose.ui.graphics.Color

// 🍑 Peach Theme Palette
val Peach       = Color(0xFFFFCBA4)   // softer peach
val PeachDeep   = Color(0xFFFF9E80)   // deeper peach accent
val SoftRose    = Color(0xFFF6A5C0)   // cozy pinkish rose
val Vanilla     = Color(0xFFFFF3E6)   // warm cream/vanilla
val Mint        = Color(0xFFA8D5BA)   // soft mint green for contrast
val CocoaBrown  = Color(0xFF8D6E63)   // light brown for grounding
val Charcoal    = Color(0xFF3E3E3E)   // dark gray for text in dark mode

// Light Mode Cozy Peach
val md_theme_light_primary = Peach
val md_theme_light_onPrimary = Color.White
val md_theme_light_primaryContainer = PeachDeep
val md_theme_light_onPrimaryContainer = Color.White

val md_theme_light_secondary = SoftRose
val md_theme_light_onSecondary = Color.White
val md_theme_light_secondaryContainer = Vanilla
val md_theme_light_onSecondaryContainer = CocoaBrown

val md_theme_light_background = Vanilla
val md_theme_light_onBackground = Charcoal

val md_theme_light_surface = Vanilla
val md_theme_light_onSurface = Charcoal

// Dark Mode Cozy Peach
val md_theme_dark_primary = PeachDeep
val md_theme_dark_onPrimary = Color.Black
val md_theme_dark_primaryContainer = CocoaBrown
val md_theme_dark_onPrimaryContainer = Vanilla

val md_theme_dark_secondary = Mint
val md_theme_dark_onSecondary = Color.Black
val md_theme_dark_secondaryContainer = CocoaBrown
val md_theme_dark_onSecondaryContainer = Vanilla

val md_theme_dark_background = Charcoal
val md_theme_dark_onBackground = Vanilla

val md_theme_dark_surface = Charcoal
val md_theme_dark_onSurface = Vanilla
