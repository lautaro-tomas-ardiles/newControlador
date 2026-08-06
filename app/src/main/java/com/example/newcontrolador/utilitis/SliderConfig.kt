package com.example.newcontrolador.utilitis

import androidx.compose.ui.graphics.painter.Painter
import com.example.newcontrolador.data.enums.SliderType

data class SliderConfig(
	val value: Float,
	val onValueChange: (Float) -> Unit,
	val valueRange: ClosedFloatingPointRange<Float>,
	val ruta: Painter,
	val sliderType: SliderType
)