package com.example.newcontrolador.connection.data

import androidx.compose.ui.graphics.painter.Painter

data class SliderConfig(
	val value: Float,
	val onValueChange: (Float) -> Unit,
	val valueRange: ClosedFloatingPointRange<Float>,
	val ruta: Painter,
	val typeForReset: Buttons = Buttons.HEIGHT
)
