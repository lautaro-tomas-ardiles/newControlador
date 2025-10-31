package com.example.newcontrolador.utilitis

import com.example.newcontrolador.connection.data.ThemeType

data class ThemeConfig(
	val isColorSelected: Boolean,
	val onClick: () -> Unit,
	val theme: ThemeType
)