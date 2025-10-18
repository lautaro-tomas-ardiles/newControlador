package com.example.newcontrolador.connection.data

data class ConfigDirections(
	val upChar: Char = 'F',
	val downChar: Char = 'B',
	val leftChar: Char = 'L',
	val rightChar: Char = 'R',
	val upLeftChar: Char = 'G',
	val upRightChar: Char = 'I',
	val downLeftChar: Char = 'H',
	val downRightChar: Char = 'J',
	val stopChar: Char = 'S'
)