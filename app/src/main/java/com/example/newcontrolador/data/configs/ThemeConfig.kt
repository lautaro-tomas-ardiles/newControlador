package com.example.newcontrolador.data.configs

import com.example.newcontrolador.data.enums.ThemeType

data class ThemeConfig(
	val isColorSelected: Boolean,
	val onClick: () -> Unit,
	val theme: ThemeType
)