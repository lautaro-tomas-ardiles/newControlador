package com.example.newcontrolador.connection

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
		fun fromSet(directions: Set<Directions>): Directions {
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
	}
}
