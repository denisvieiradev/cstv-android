package com.denisvieiradev.design_system.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.denisvieiradev.design_system.R

val Roboto = FontFamily(
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_bold, FontWeight.Bold)
)

val AppTypography = Typography(
    displayLarge = TextStyle(fontWeight = FontWeight.Normal, fontSize = 57.sp, lineHeight = 64.sp, letterSpacing = (-0.25).sp, fontFamily = Roboto),
    displayMedium = TextStyle(fontWeight = FontWeight.Normal, fontSize = 45.sp, lineHeight = 52.sp, letterSpacing = 0.sp, fontFamily = Roboto),
    displaySmall = TextStyle(fontWeight = FontWeight.Normal, fontSize = 36.sp, lineHeight = 44.sp, letterSpacing = 0.sp, fontFamily = Roboto),
    headlineLarge = TextStyle(fontWeight = FontWeight.Normal, fontSize = 32.sp, lineHeight = 40.sp, letterSpacing = 0.sp, fontFamily = Roboto),
    headlineMedium = TextStyle(fontWeight = FontWeight.Normal, fontSize = 28.sp, lineHeight = 36.sp, letterSpacing = 0.sp, fontFamily = Roboto),
    headlineSmall = TextStyle(fontWeight = FontWeight.Normal, fontSize = 24.sp, lineHeight = 32.sp, letterSpacing = 0.sp, fontFamily = Roboto),
    titleLarge = TextStyle(fontWeight = FontWeight.Normal, fontSize = 22.sp, lineHeight = 28.sp, letterSpacing = 0.sp, fontFamily = Roboto),
    titleMedium = TextStyle(fontWeight = FontWeight.Medium, fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = (0.15).sp, fontFamily = Roboto),
    titleSmall = TextStyle(fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = (0.1).sp, fontFamily = Roboto),
    labelLarge = TextStyle(fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = (0.1).sp, fontFamily = Roboto),
    labelMedium = TextStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = (0.5).sp, fontFamily = Roboto),
    labelSmall = TextStyle(fontWeight = FontWeight.Medium, fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = (0.5).sp, fontFamily = Roboto),
    bodyLarge = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = (0.5).sp, fontFamily = Roboto),
    bodyMedium = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = (0.25).sp, fontFamily = Roboto),
    bodySmall = TextStyle(fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = (0.4).sp, fontFamily = Roboto),
)
