package com.example.newcontrolador.utilitis

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.newcontrolador.connection.data.ThemeType
import com.example.newcontrolador.ui.theme.DarckDefault
import com.example.newcontrolador.ui.theme.LightDefault

@Composable
fun ThemeItem(
	isColorSelected: Boolean,
	onClick: (ThemeType) -> Unit,
	theme: ThemeType
) {
	val backgroundColor = when (theme) {
		ThemeType.DEFAULT -> DarckDefault.background
		ThemeType.WHITE -> LightDefault.background
	}
	val borderColor = when (theme) {
		ThemeType.DEFAULT -> DarckDefault.primary
		ThemeType.WHITE -> LightDefault.primary
	}

	Box(
		modifier = Modifier
			.clickable { onClick(theme) }
			.background(backgroundColor)
			.size(45.dp)
			.border(
				width = 3.dp,
				color = if (isColorSelected) borderColor else Color.Transparent
			)

	)
}
