package com.krist.train.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = TrainOrange,
    onPrimary = TrainInk,
    surface = TrainSurface,
    onSurface = TrainInk,
)

private val DarkColors = darkColorScheme(
    primary = TrainOrange,
)

@Composable
fun TrAInTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = TrainTypography,
        content = content,
    )
}
