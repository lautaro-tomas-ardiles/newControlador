package com.example.newcontrolador.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.example.newcontrolador.data.enums.ThemeType
import com.example.newcontrolador.storage.DataStoreViewModel

val defaultScheme = darkColorScheme(
	primary = blue10,
	secondary = yellow20,
	tertiary = green20,
	onSecondary = yellow10,
	onTertiary = green10,
	background = black10,
	onBackground = black20
)
val lightScheme = lightColorScheme(
	primary = blue20,
	secondary = yellow30,
	tertiary = green40,
	onSecondary = yellow40,
	onTertiary = green30,
	background = white10,
	onBackground = blue30
)
fun customScheme(
	primary: Long,
	secondary: Long,
	tertiary: Long,
	background: Long,
	onSecondary: Long,
	onTertiary: Long,
	onBackground: Long
) = darkColorScheme(
	primary = Color(primary),
	secondary = Color(secondary),
	tertiary = Color(tertiary),
	background = Color(background),
	onSecondary = Color(onSecondary),
	onTertiary = Color(onTertiary),
	onBackground = Color(onBackground)
)

@Composable
fun NewControladorTheme(
	themeType: ThemeType = ThemeType.DEFAULT,
	viewModel: DataStoreViewModel,
	content: @Composable () -> Unit
) {
	val colors by viewModel.colors.collectAsState()

	val customColorSheme = customScheme(
		primary = colors.primary,
		secondary = colors.secondary, onSecondary = colors.onSecondary,
		tertiary = colors.tertiary, onTertiary = colors.onTertiary,
		background = colors.background, onBackground = colors.onBackground
	)
	val colorScheme = when (themeType) {
		ThemeType.DEFAULT -> defaultScheme
		ThemeType.WHITE -> lightScheme
		ThemeType.CUSTOM -> customColorSheme
	}

	MaterialTheme(
		colorScheme = colorScheme,
		typography = Typography,
		content = content
	)
}