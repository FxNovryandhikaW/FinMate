package com.example.finmate.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = PurplePrimary,
    secondary = PurpleSecondary,
    tertiary = PurpleTertiary,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = TextWhite,
    onSurface = TextWhite,
    onBackground = TextWhite,
    error = ExpenseRed
)

@Composable
fun FinMateTheme(
    darkTheme: Boolean = true, // Force dark theme as per design
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
