package com.mintifi.ceoos.ui.theme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object MintifiColors {
    val Background = Color(0xFFF8F9FB); val Surface = Color(0xFFFFFFFF)
    val Surface2 = Color(0xFFF0F2F5); val Border = Color(0xFFE2E6EA)
    val Accent = Color(0xFF1A4FB5); val AccentBlue = Color(0xFF2563EB)
    val TextPrimary = Color(0xFF0F1729); val TextSecondary = Color(0xFF4B5675)
    val TextMuted = Color(0xFF9BA8BB)
    val P0Background = Color(0xFFFEF2F2); val P0Text = Color(0xFFDC2626)
    val P1Background = Color(0xFFFFFBEB); val P1Text = Color(0xFFD97706)
    val P2Background = Color(0xFFEFF6FF); val P2Text = Color(0xFF2563EB)
    val P3Background = Color(0xFFF0FDF4); val P3Text = Color(0xFF16A34A)
    val DangerRed = Color(0xFFDC2626); val WarnAmber = Color(0xFFD97706)
    val SuccessGreen = Color(0xFF16A34A); val InfoBlue = Color(0xFF2563EB)
}

private val LightColors = lightColorScheme(
    primary = MintifiColors.Accent, onPrimary = Color.White,
    background = MintifiColors.Background, onBackground = MintifiColors.TextPrimary,
    surface = MintifiColors.Surface, onSurface = MintifiColors.TextPrimary,
    surfaceVariant = MintifiColors.Surface2, onSurfaceVariant = MintifiColors.TextSecondary,
    outline = MintifiColors.Border, error = MintifiColors.DangerRed
)

@Composable
fun MintifiTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = LightColors, typography = Typography(
        headlineLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 24.sp),
        headlineMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 20.sp),
        headlineSmall = TextStyle(fontWeight = FontWeight.Medium, fontSize = 17.sp),
        titleLarge = TextStyle(fontWeight = FontWeight.Medium, fontSize = 16.sp),
        titleMedium = TextStyle(fontWeight = FontWeight.Medium, fontSize = 14.sp),
        titleSmall = TextStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp),
        bodyLarge = TextStyle(fontWeight = FontWeight.Normal, fontSize = 15.sp, lineHeight = 22.sp),
        bodyMedium = TextStyle(fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 20.sp),
        bodySmall = TextStyle(fontWeight = FontWeight.Normal, fontSize = 11.sp),
        labelLarge = TextStyle(fontWeight = FontWeight.Medium, fontSize = 13.sp),
        labelMedium = TextStyle(fontWeight = FontWeight.Medium, fontSize = 11.sp),
        labelSmall = TextStyle(fontWeight = FontWeight.Medium, fontSize = 10.sp)
    ), content = content)
}
