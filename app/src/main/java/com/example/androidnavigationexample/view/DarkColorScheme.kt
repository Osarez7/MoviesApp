package com.example.androidnavigationexample.view

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = GoldAccent,
    onPrimary = DeepBlack,
    primaryContainer = DeepGold,
    onPrimaryContainer = OffWhite,
    
    secondary = CrimsonRed,
    onSecondary = OffWhite,
    secondaryContainer = BurntOrange,
    onSecondaryContainer = OffWhite,
    
    tertiary = BurntOrange,
    onTertiary = OffWhite,
    
    background = DeepBlack,
    onBackground = OffWhite,
    
    surface = RichBlack,
    onSurface = OffWhite,
    surfaceVariant = CharcoalGray,
    onSurfaceVariant = LightGray,
    
    error = ErrorRed,
    onError = OffWhite,
    
    outline = SlateGray,
    outlineVariant = DarkGray
)

@Composable
fun TMDBMoviesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}