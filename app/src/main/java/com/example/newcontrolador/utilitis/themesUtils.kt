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
import com.example.newcontrolador.data.enums.ThemeType
import com.example.newcontrolador.ui.theme.defaultScheme
import com.example.newcontrolador.ui.theme.lightScheme

@Composable
fun ThemeItem(
	isColorSelected: Boolean,
	onClick: (ThemeType) -> Unit,
	theme: ThemeType
) {
	val backgroundColor = when (theme) {
		ThemeType.DEFAULT -> defaultScheme.background
		ThemeType.WHITE -> lightScheme.background
		ThemeType.CUSTOM -> defaultScheme.background
	}
	val borderColor = when (theme) {
		ThemeType.DEFAULT -> defaultScheme.primary
		ThemeType.WHITE -> lightScheme.primary
		ThemeType.CUSTOM -> defaultScheme.primary
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
