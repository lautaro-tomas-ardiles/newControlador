package com.example.newcontrolador.data.enums

enum class ModesEnum(var char: Char) {
	MANUAL('C'),
	AUTOMATA('A');

	companion object {
		fun getModeName(modesEnum: ModesEnum): String {
			return when (modesEnum) {
				MANUAL -> "control manual"
				AUTOMATA -> "automata"
			}
		}
	}
}