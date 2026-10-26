package com.kintukka.hanger.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kintukka.hanger.R

// Palette: warm paper + ink with a fashion rose accent and a gold highlight.
private val Paper = Color(0xFFFAF7F4)
private val Ink = Color(0xFF1A1614)
private val Rose = Color(0xFFBE185D)
private val RoseContainer = Color(0xFFFCE7F0)
private val OnRoseContainer = Color(0xFF5B0B2E)
private val Gold = Color(0xFFB45309)
private val GoldContainer = Color(0xFFFDF0DC)
private val Muted = Color(0xFF6B625C)
private val Line = Color(0xFFE7DED7)
private val Tile = Color(0xFFF1ECE6)

private val NightPaper = Color(0xFF141112)
private val NightSurface = Color(0xFF1D191A)
private val NightTile = Color(0xFF2A2426)
private val NightInk = Color(0xFFF4EEEA)
private val NightRose = Color(0xFFF472B6)
private val NightMuted = Color(0xFFB3A9A3)

private val LightColors = lightColorScheme(
    primary = Rose,
    onPrimary = Color.White,
    primaryContainer = RoseContainer,
    onPrimaryContainer = OnRoseContainer,
    secondary = Gold,
    onSecondary = Color.White,
    secondaryContainer = GoldContainer,
    onSecondaryContainer = Color(0xFF4A2205),
    tertiary = Color(0xFF3F6B5A),
    tertiaryContainer = Color(0xFFDCEFE6),
    onTertiaryContainer = Color(0xFF0F2E22),
    background = Paper,
    onBackground = Ink,
    surface = Paper,
    onSurface = Ink,
    surfaceVariant = Tile,
    onSurfaceVariant = Muted,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF7F2EE),
    surfaceContainer = Color(0xFFF3EEE9),
    surfaceContainerHigh = Color(0xFFEDE7E1),
    surfaceContainerHighest = Color(0xFFE7E0DA),
    outline = Color(0xFFB9AEA6),
    outlineVariant = Line,
    error = Color(0xFFB3261E),
    inverseSurface = Ink,
    inverseOnSurface = Paper,
)

private val DarkColors = darkColorScheme(
    primary = NightRose,
    onPrimary = Color(0xFF3B0A1F),
    primaryContainer = Color(0xFF5B1A38),
    onPrimaryContainer = Color(0xFFFFD9E6),
    secondary = Color(0xFFF59E0B),
    onSecondary = Color(0xFF2E1A00),
    secondaryContainer = Color(0xFF4A3410),
    onSecondaryContainer = Color(0xFFFFE2B3),
    tertiary = Color(0xFF8CC7AE),
    tertiaryContainer = Color(0xFF1F3A30),
    onTertiaryContainer = Color(0xFFCDEBDD),
    background = NightPaper,
    onBackground = NightInk,
    surface = NightPaper,
    onSurface = NightInk,
    surfaceVariant = NightTile,
    onSurfaceVariant = NightMuted,
    surfaceContainerLowest = Color(0xFF0F0D0E),
    surfaceContainerLow = NightSurface,
    surfaceContainer = Color(0xFF221D1F),
    surfaceContainerHigh = Color(0xFF2B2527),
    surfaceContainerHighest = Color(0xFF342D30),
    outline = Color(0xFF7D726C),
    outlineVariant = Color(0xFF3A3234),
    inverseSurface = NightInk,
    inverseOnSurface = NightPaper,
)

val Inter = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semibold, FontWeight.SemiBold),
    Font(R.font.inter_bold, FontWeight.Bold),
)

val Serif = FontFamily(
    Font(R.font.instrument_serif_regular, FontWeight.Normal),
    Font(R.font.instrument_serif_italic, FontWeight.Normal, FontStyle.Italic),
)

private val base = Typography()

val HangerTypography = Typography(
    displayLarge = TextStyle(fontFamily = Serif, fontSize = 52.sp, lineHeight = 56.sp, letterSpacing = (-0.5).sp),
    displayMedium = TextStyle(fontFamily = Serif, fontSize = 42.sp, lineHeight = 46.sp, letterSpacing = (-0.3).sp),
    displaySmall = TextStyle(fontFamily = Serif, fontSize = 34.sp, lineHeight = 38.sp),
    headlineLarge = TextStyle(fontFamily = Serif, fontSize = 32.sp, lineHeight = 36.sp),
    headlineMedium = TextStyle(fontFamily = Serif, fontSize = 28.sp, lineHeight = 32.sp),
    headlineSmall = TextStyle(fontFamily = Serif, fontSize = 24.sp, lineHeight = 28.sp),
    titleLarge = base.titleLarge.copy(fontFamily = Inter, fontWeight = FontWeight.SemiBold, fontSize = 20.sp),
    titleMedium = base.titleMedium.copy(fontFamily = Inter, fontWeight = FontWeight.SemiBold),
    titleSmall = base.titleSmall.copy(fontFamily = Inter, fontWeight = FontWeight.SemiBold),
    bodyLarge = base.bodyLarge.copy(fontFamily = Inter),
    bodyMedium = base.bodyMedium.copy(fontFamily = Inter),
    bodySmall = base.bodySmall.copy(fontFamily = Inter),
    labelLarge = base.labelLarge.copy(fontFamily = Inter, fontWeight = FontWeight.SemiBold),
    labelMedium = base.labelMedium.copy(fontFamily = Inter, fontWeight = FontWeight.Medium),
    labelSmall = base.labelSmall.copy(fontFamily = Inter, fontWeight = FontWeight.Medium),
)

val HangerShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp),
)

/** Extra tokens Material doesn't model. */
@Immutable
data class HangerExtras(
    val tile: Color,
    val success: Color,
    val warning: Color,
    val laundry: Color,
)

val LocalExtras = staticCompositionLocalOf {
    HangerExtras(tile = Tile, success = Color(0xFF2F7D5B), warning = Color(0xFFB45309), laundry = Color(0xFF2563EB))
}

@Composable
fun HangerTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors: ColorScheme = if (darkTheme) DarkColors else LightColors
    val extras = if (darkTheme) {
        HangerExtras(tile = NightTile, success = Color(0xFF6FCF97), warning = Color(0xFFF5B041), laundry = Color(0xFF7FB2FF))
    } else {
        HangerExtras(tile = Tile, success = Color(0xFF2F7D5B), warning = Color(0xFFB45309), laundry = Color(0xFF2563EB))
    }
    CompositionLocalProvider(LocalExtras provides extras) {
        MaterialTheme(colorScheme = colors, typography = HangerTypography, shapes = HangerShapes, content = content)
    }
}

object HangerTokens {
    val extras: HangerExtras
        @Composable get() = LocalExtras.current
}
