package com.example.newcontrolador.connection.data

enum class Directions() {
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
		fun getChar(directions: Directions, config: DirectionsConfig): Char {
			return when (directions) {
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

		private fun fromSet(directions: Set<Directions>): Directions {
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

		fun charFromSet(directions: Set<Directions>, directionsConfig: DirectionsConfig): Char {
			return getChar(Directions.fromSet(directions), directionsConfig)
		}

		fun getDirectionsName(directions: Directions): String {
			return when (directions) {
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