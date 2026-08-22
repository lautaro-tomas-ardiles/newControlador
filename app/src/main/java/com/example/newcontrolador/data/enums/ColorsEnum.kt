package com.example.newcontrolador.data.enums

enum class ColorsEnum {
	PRIMARY,
    SECONDARY,
    ON_SECONDARY,
    TERTIARY,
    ON_TERTIARY,
    BACKGROUND,
    ON_BACKGROUND;

    companion object {
        fun nameString(value: ColorsEnum): String {
            return when (value) {
                PRIMARY -> "primario"
                SECONDARY -> "secundario"
                ON_SECONDARY -> "segundo secundario"
                TERTIARY -> "terciario"
                ON_TERTIARY -> "segundo terciario"
                BACKGROUND -> "fondo"
                ON_BACKGROUND -> "segundo fondo"
            }
        }
    }
}