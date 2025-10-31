package com.example.newcontrolador.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.newcontrolador.connection.data.ThemeType

val DarckDefault = darkColorScheme(
	primary = blue10,
	secondary = yellow20,
	tertiary = green20,
	onSecondary = yellow10,
	onTertiary = green10,
	background = black10,
	onBackground = black20
)
val LightDefault = lightColorScheme(
	primary = blue20,
	secondary = yellow30,
	tertiary = green40,
	onSecondary = yellow40,
	onTertiary = green30,
	background = white10,
	onBackground = red10
)
val Scheme = lightColorScheme(
	primary = yellow30,
	secondary = green40,
	tertiary = yellow40,
	onSecondary = green30,
	onTertiary = red10,
	background = white10,
	onBackground = blue20
)

@Composable
fun NewControladorTheme(
	themeType: ThemeType = ThemeType.DEFAULT,
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

		themeType == ThemeType.DEFAULT -> DarckDefault
		themeType == ThemeType.WHITE -> LightDefault
		//themeType == ThemeType.WHITE_2 -> Scheme
		else -> DarckDefault
	}

	MaterialTheme(
		colorScheme = colorScheme,
		typography = Typography,
		content = content
	)
}