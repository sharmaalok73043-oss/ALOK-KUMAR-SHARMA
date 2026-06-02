package com.example.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Original static colors for reference / legacy compatibility
val GeoAccentDeep = Color(0xFF77574E)   // Deep rich brown
val GeoBackground = Color(0xFFFAF9F6)   // Warm cream/ivory
val GeoTextPrimary = Color(0xFF2A2422)  // Deep brown/charcoal
val GeoTextSecondary = Color(0xFF7E7470)// Subtitle gray-clay
val GeoBeigeMuted = Color(0xFFEBDBCF)   // Soft beige
val GeoLavishAvatar = Color(0xFFEADDFF) // Muted lavender
val GeoLavishBorder = Color(0xFFD0BCFF) // Avatar border
val GeoSlateChemistry = Color(0xFFD9E2FF)// Soft chemistry blue
val GeoLightCream = Color(0xFFF3EFEC)   // Contrast background fill

data class ThemeColors(
    val primaryBrown: Color,
    val pastelPeach: Color,
    val softCreamSurface: Color,
    val backgroundBeige: Color,
    val textDeepPrivate: Color,
    val textMutedGray: Color,
    val symmetricalBlue: Color,
    val correctGreen: Color,
    val incorrectRed: Color,
    val selectionBlue: Color
)

val ClassicWarmColors = ThemeColors(
    primaryBrown = Color(0xFF77574E),
    pastelPeach = Color(0xFFEBDBCF),
    softCreamSurface = Color(0xFFF3EFEC),
    backgroundBeige = Color(0xFFFAF9F6),
    textDeepPrivate = Color(0xFF2A2422),
    textMutedGray = Color(0xFF7E7470),
    symmetricalBlue = Color(0xFFD9E2FF),
    correctGreen = Color(0xFF4CAF50),
    incorrectRed = Color(0xFFE53935),
    selectionBlue = Color(0xFF2196F3)
)

val CosmicNightColors = ThemeColors(
    primaryBrown = Color(0xFF9E82F1),
    pastelPeach = Color(0xFF3D305A),
    softCreamSurface = Color(0xFF271F3B),
    backgroundBeige = Color(0xFF130F21),
    textDeepPrivate = Color(0xFFFFFFFF),
    textMutedGray = Color(0xFFA69CBD),
    symmetricalBlue = Color(0xFF2C5070),
    correctGreen = Color(0xFF00E676),
    incorrectRed = Color(0xFFFF1744),
    selectionBlue = Color(0xFF2979FF)
)

val TealOceanColors = ThemeColors(
    primaryBrown = Color(0xFF00796B),
    pastelPeach = Color(0xFFB2DFDB),
    softCreamSurface = Color(0xFFE0F2F1),
    backgroundBeige = Color(0xFFF0FDFB),
    textDeepPrivate = Color(0xFF004D40),
    textMutedGray = Color(0xFF546E7A),
    symmetricalBlue = Color(0xFFB3E5FC),
    correctGreen = Color(0xFF2E7D32),
    incorrectRed = Color(0xFFC62828),
    selectionBlue = Color(0xFF0277BD)
)

val ForestMossColors = ThemeColors(
    primaryBrown = Color(0xFF3E6B47),
    pastelPeach = Color(0xFFC8E6C9),
    softCreamSurface = Color(0xFFE8F5E9),
    backgroundBeige = Color(0xFFFAFDFB),
    textDeepPrivate = Color(0xFF1B5E20),
    textMutedGray = Color(0xFF5D6D5E),
    symmetricalBlue = Color(0xFFFFE0B2),
    correctGreen = Color(0xFF2E7D32),
    incorrectRed = Color(0xFFC62828),
    selectionBlue = Color(0xFF1565C0)
)

val LocalAppColors = staticCompositionLocalOf { ClassicWarmColors }

// Alias names that evaluate dynamically based on active local theme key
val BackgroundBeige: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppColors.current.backgroundBeige

val TextDeepPrivate: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppColors.current.textDeepPrivate

val TextBodyDark: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppColors.current.textDeepPrivate

val TextMutedGray: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppColors.current.textMutedGray

val PrimaryBrown: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppColors.current.primaryBrown

val PastelPeach: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppColors.current.pastelPeach

val SoftCreamSurface: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppColors.current.softCreamSurface

val SymmetricalBlue: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppColors.current.symmetricalBlue

val LavenderAccent = Color(0xFFEADDFF)
val DeepViolet = Color(0xFF21005D)
val BorderColor = Color(0xFFD0BCFF)

val CorrectGreen: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppColors.current.correctGreen

val IncorrectRed: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppColors.current.incorrectRed

val SelectionBlue: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppColors.current.selectionBlue
