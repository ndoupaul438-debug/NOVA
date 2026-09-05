package com.nova.app.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

enum class NovaThemeStyle(
    val displayName: String,
    val primary: Color,
    val secondary: Color,
    val tertiary: Color
) {
    OCEAN(
        "Ocean Blue",
        Color(0xFF2563EB),
        Color(0xFF06B6D4),
        Color(0xFF38BDF8)
    ),
    ROYAL(
        "Royal Purple",
        Color(0xFF7C3AED),
        Color(0xFFA855F7),
        Color(0xFFEC4899)
    ),
    EMERALD(
        "Emerald",
        Color(0xFF059669),
        Color(0xFF10B981),
        Color(0xFF34D399)
    ),
    SUNSET(
        "Sunset",
        Color(0xFFF97316),
        Color(0xFFEF4444),
        Color(0xFFEAB308)
    ),
    CRIMSON(
        "Crimson",
        Color(0xFFDC2626),
        Color(0xFFBE123C),
        Color(0xFFF43F5E)
    ),
    CYBER(
        "Cyber Cyan",
        Color(0xFF0891B2),
        Color(0xFF06B6D4),
        Color(0xFF22D3EE)
    ),
    MIDNIGHT(
        "Midnight",
        Color(0xFF4F46E5),
        Color(0xFF6366F1),
        Color(0xFF8B5CF6)
    ),
    AURORA(
        "Aurora",
        Color(0xFF14B8A6),
        Color(0xFF8B5CF6),
        Color(0xFFEC4899)
    )
}

@Composable
fun NovaTheme(
    themeStyle: NovaThemeStyle = NovaThemeStyle.OCEAN,
    darkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val style = themeStyle

    val colors = if (darkMode) {
        darkColorScheme(
            primary = style.primary,
            secondary = style.secondary,
            tertiary = style.tertiary
        )
    } else {
        lightColorScheme(
            primary = style.primary,
            secondary = style.secondary,
            tertiary = style.tertiary
        )
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography(),
        content = content
    )
}
