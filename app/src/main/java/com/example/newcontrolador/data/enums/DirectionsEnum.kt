package com.example.newcontrolador.data.enums

import com.example.newcontrolador.data.configs.DirectionsConfig

enum class DirectionsEnum() {
	UP,
	DOWN,
	LEFT,
	RIGHT,
	UP_LEFT,
	UP_RIGHT,
	DOWN_LEFT,
	DOWN_RIGHT,
	STOP;

	companion object {
		fun getChar(directionsEnum: DirectionsEnum, config: DirectionsConfig): Char {
			return when (directionsEnum) {
				UP -> config.upChar
				DOWN -> config.downChar
				LEFT -> config.leftChar
				RIGHT -> config.rightChar
				UP_LEFT -> config.upLeftChar
				UP_RIGHT -> config.upRightChar
				DOWN_LEFT -> config.downLeftChar
				DOWN_RIGHT -> config.downRightChar
				STOP -> config.stopChar
			}
		}

		private fun fromSet(directions: Set<DirectionsEnum>): DirectionsEnum {
			return when {
				directions.contains(UP) && directions.contains(LEFT) -> UP_LEFT
				directions.contains(UP) && directions.contains(RIGHT) -> UP_RIGHT
				directions.contains(DOWN) && directions.contains(LEFT) -> DOWN_LEFT
				directions.contains(DOWN) && directions.contains(RIGHT) -> DOWN_RIGHT
				directions.contains(UP) -> UP
				directions.contains(DOWN) -> DOWN
				directions.contains(LEFT) -> LEFT
				directions.contains(RIGHT) -> RIGHT
				else -> STOP
			}
		}

		fun charFromSet(directions: Set<DirectionsEnum>, directionsConfig: DirectionsConfig): Char {
			return getChar(DirectionsEnum.fromSet(directions), directionsConfig)
		}

		fun getDirectionsName(directionsEnum: DirectionsEnum): String {
			return when (directionsEnum) {
				UP -> "Arriba"
				DOWN -> "Abajo"
				LEFT -> "Izquierda"
				RIGHT -> "Derecha"
				UP_LEFT -> "Arriba Izquierda"
				UP_RIGHT -> "Arriba Derecha"
				DOWN_LEFT -> "Abajo Izquierda"
				DOWN_RIGHT -> "Abajo Derecha"
				STOP -> "Detener"
			}
		}
	}
}