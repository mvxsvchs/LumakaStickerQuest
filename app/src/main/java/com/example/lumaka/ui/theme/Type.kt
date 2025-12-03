package com.example.lumaka.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.lumaka.R

private val OpenDyslexic = FontFamily(
    Font(R.font.open_dyslexic_regular, FontWeight.Normal),
    Font(R.font.open_dyslexic_bold, FontWeight.Bold),
    Font(R.font.open_dyslexic_italic, FontWeight.Normal, style = FontStyle.Italic),
    Font(R.font.open_dyslexic_bold_italic, FontWeight.Bold, style = FontStyle.Italic)
)

private val BaseTypography = Typography()

val Typography = Typography(
    displayLarge = BaseTypography.displayLarge.copy(fontFamily = OpenDyslexic),
    displayMedium = BaseTypography.displayMedium.copy(fontFamily = OpenDyslexic),
    displaySmall = BaseTypography.displaySmall.copy(fontFamily = OpenDyslexic),

    headlineLarge = BaseTypography.headlineLarge.copy(fontFamily = OpenDyslexic),
    bodyLarge = TextStyle(
        fontFamily = OpenDyslexic,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = BaseTypography.bodyMedium.copy(fontFamily = OpenDyslexic),
    bodySmall = BaseTypography.bodySmall.copy(fontFamily = OpenDyslexic),

    titleLarge = BaseTypography.titleLarge.copy(fontFamily = OpenDyslexic),
    titleMedium = BaseTypography.titleMedium.copy(fontFamily = OpenDyslexic),
    titleSmall = BaseTypography.titleSmall.copy(fontFamily = OpenDyslexic),

    labelLarge = TextStyle(
        fontFamily = OpenDyslexic,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    labelMedium = BaseTypography.labelMedium.copy(fontFamily = OpenDyslexic),
    labelSmall = BaseTypography.labelSmall.copy(fontFamily = OpenDyslexic),

    headlineSmall = TextStyle(
        fontFamily = OpenDyslexic,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = OpenDyslexic,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    )
)
