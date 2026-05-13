package com.example.newcontrolador.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

val DarckDefault = darkColorScheme(
	primary = blue10,
	secondary = yellow20,
	tertiary = green20,
	onSecondary = yellow10,
	onTertiary = green10,
	background = black10,
	onBackground = black20
)

@Composable
fun NewControladorTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	// Dynamic color is available on Android 12+
	dynamicColor: Boolean = false,
	content: @Composable () -> Unit
) {
	val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarckDefault
        else -> DarckDefault
    }

	MaterialTheme(
		colorScheme = colorScheme,
		typography = Typography,
		content = content
	)
}