package com.example.newcontrolador.connection.data

enum class Modes(var char: Char) {
	MANUAL('C'),
	AUTOMATA('A');

	companion object {
		fun getModeName(modes: Modes): String {
			return when (modes) {
				MANUAL -> "control manual"
				AUTOMATA -> "automata"
			}
		}
	}
}