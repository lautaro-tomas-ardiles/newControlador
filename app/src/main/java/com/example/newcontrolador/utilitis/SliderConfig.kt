package com.example.newcontrolador.utilitis

import androidx.compose.ui.graphics.painter.Painter
import com.example.newcontrolador.connection.data.MovementEnum

data class SliderConfig(
	val value: Float,
	val onValueChange: (Float) -> Unit,
	val valueRange: ClosedFloatingPointRange<Float>,
	val ruta: Painter,
	val typeForReset: MovementEnum? = MovementEnum.HEIGHT
)