package com.example.newcontrolador.utilitis

data class DefaultButtonSize(
	val width: Float = 165f,
	val height: Float = 150f,
	val padding: Float = 0f
)
enum class ButtonSize {
	WIDTH,
	HEIGHT,
	PADDING
}
