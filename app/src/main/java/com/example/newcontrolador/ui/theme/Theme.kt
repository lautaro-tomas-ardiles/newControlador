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

private val Default = darkColorScheme(
	primary = DarckThemeBlue,
	secondary = DarckThemeLightYellow,
	tertiary = DarckThemeLightGreen,
	onSecondary = DarckThemeDarkYellow,
	onTertiary = DarckThemeDarkGreen,
	background = DarckThemeBlack,
	onBackground = DarckThemeLightBlack
)
private val BlueScheme = lightColorScheme(
	primary = LightThemeBlue,
	secondary = LightThemeLightRed,
	tertiary = LightThemeDarckGreen,
	onSecondary = LightThemeDarckRed,
	onTertiary = LightThemeLigthGreen,
	background = LightThemeDarckWhite,
	onBackground = LightThemeLightWhite
)
private val Scheme = lightColorScheme(
	primary = LightThemeLightRed,
	secondary = LightThemeDarckGreen,
	tertiary = LightThemeDarckRed,
	onSecondary = LightThemeLigthGreen,
	onTertiary = LightThemeDarckWhite,
	background = LightThemeLightWhite,
	onBackground = LightThemeBlue
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

		themeType == ThemeType.BLUE -> BlueScheme
		themeType == ThemeType.GREEN -> Scheme
		else -> Default
	}

	MaterialTheme(
		colorScheme = colorScheme,
		typography = Typography,
		content = content
	)
}