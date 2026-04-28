package com.flash.targaryen.ui.theme


import android.app.Activity
import android.os.Build
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

// ── Futuristic Neon Palette ──────────────────────────────────────────────────
val NeonCyan       = Color(0xFF00E5FF)
val NeonCyanDim    = Color(0xFF00B8CC)
val NeonPurple     = Color(0xFF7C4DFF)
val NeonPurpleDim  = Color(0xFF5C35CC)
val DeepSpace      = Color(0xFF050A1A)
val SpaceBlue      = Color(0xFF0D1B2A)
val GridLine       = Color(0xFF1A2744)
val StarWhite      = Color(0xFFF0F8FF)
val PlasmaGlow     = Color(0xFF00FFC6)
val WarningAmber   = Color(0xFFFFAB00)

// ── Dark Color Scheme ────────────────────────────────────────────────────────
private val DarkColors = darkColorScheme(
    primary          = NeonCyan,
    onPrimary        = DeepSpace,
    primaryContainer = Color(0xFF003D4D),
    onPrimaryContainer = NeonCyan,
    secondary        = NeonPurple,
    onSecondary      = StarWhite,
    secondaryContainer = Color(0xFF1A0A3D),
    onSecondaryContainer = NeonPurple,
    tertiary         = PlasmaGlow,
    background       = DeepSpace,
    onBackground     = StarWhite,
    surface          = SpaceBlue,
    onSurface        = StarWhite,
    surfaceVariant   = GridLine,
    onSurfaceVariant = Color(0xFF8BA3BF),
    outline          = Color(0xFF1E3555),
    error            = Color(0xFFFF4060),
)

// ── Light Color Scheme ───────────────────────────────────────────────────────
private val LightColors = lightColorScheme(
    primary          = Color(0xFF006680),
    onPrimary        = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFB8EEFF),
    onPrimaryContainer = Color(0xFF001F28),
    secondary        = Color(0xFF5B3DC8),
    onSecondary      = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE8DEFF),
    onSecondaryContainer = Color(0xFF16005A),
    tertiary         = Color(0xFF007A5E),
    background       = Color(0xFFF4FBFF),
    onBackground     = Color(0xFF001F28),
    surface          = Color(0xFFFFFFFF),
    onSurface        = Color(0xFF001F28),
    surfaceVariant   = Color(0xFFDBEDF5),
    onSurfaceVariant = Color(0xFF3F5864),
    outline          = Color(0xFF6F9098),
    error            = Color(0xFFBA1A1A),
)

@Composable
fun NexusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}