package com.example.newcontrolador.connection.data

enum class Directions(var char: Char) {
	UP('F'),
	DOWN('B'),
	LEFT('L'),
	RIGHT('R'),
	UP_LEFT('G'),
	UP_RIGHT('I'),
	DOWN_LEFT('H'),
	DOWN_RIGHT('J'),
	STOP('S');

	companion object {
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

		fun charFromSet(directions: Set<Directions>): Char {
			return Directions.fromSet(directions).char
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