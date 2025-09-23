package com.example.newcontrolador.utilitis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.newcontrolador.ui.theme.Black2

@Composable
fun CodigoArduino(text: String) {
	Box(
		Modifier
			.background(Black2)
			.padding(16.dp)
	) {
		Text(
			text = text.trimIndent(),
			color = Color.White,
			fontFamily = FontFamily.Monospace
		)
	}
}