package com.example.newcontrolador.utilitis

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.newcontrolador.connection.data.Directions
import com.example.newcontrolador.connection.data.Modes
import com.example.newcontrolador.connection.data.SliderConfig

/**
 * Elemento de configuración para un carácter asociado a una dirección.
 *
 * Permite al usuario cambiar el carácter asignado a la dirección indicada mediante un
 * `OutlinedTextField`. Valida que solo se ingrese un carácter y muestra error en caso contrario.
 *
 * @param directions Dirección a configurar.
 */
@Composable
private fun SettingsItemForDirections(directions: Directions) {
	val text = Directions.getDirectionsName(directions)

	var newDirection by remember { mutableStateOf("") }
	var isError by remember { mutableStateOf(false) }

	Card(
		modifier = Modifier
			.wrapContentWidth()
			.padding(horizontal = 10.dp, vertical = 5.dp),
		shape = RoundedCornerShape(10),
		colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary),
		elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
		border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary)
	) {
		Spacer(Modifier.padding(5.dp))

		Row(verticalAlignment = Alignment.CenterVertically) {
			Spacer(Modifier.padding(5.dp))

			Text(text = text, color = MaterialTheme.colorScheme.background)

			Spacer(Modifier.padding(10.dp))

			OutlinedTextField(
				value = newDirection,
				onValueChange = {
					newDirection = it

					if (it.length == 1) {
						val newChar = it[0]
						when (directions) {
							Directions.UP -> Directions.UP.char = newChar
							Directions.DOWN -> Directions.DOWN.char = newChar
							Directions.LEFT -> Directions.LEFT.char = newChar
							Directions.RIGHT -> Directions.RIGHT.char = newChar
							Directions.UP_LEFT -> Directions.UP_LEFT.char = newChar
							Directions.UP_RIGHT -> Directions.UP_RIGHT.char = newChar
							Directions.DOWN_LEFT -> Directions.DOWN_LEFT.char = newChar
							Directions.DOWN_RIGHT -> Directions.DOWN_RIGHT.char = newChar
							Directions.STOP -> Directions.STOP.char = newChar
						}
						isError = false
					} else {
						isError = true
					}
				},
				placeholder = {
					Text(text = "${directions.char}", color = MaterialTheme.colorScheme.primary)
				},
				singleLine = true,
				colors = OutlinedTextFieldDefaults.colors(
					unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
					focusedBorderColor = MaterialTheme.colorScheme.onSecondary,
					focusedTextColor = MaterialTheme.colorScheme.background,
					unfocusedLabelColor = MaterialTheme.colorScheme.background
				),
				modifier = Modifier.width(50.dp),
				isError = isError
			)
			Spacer(Modifier.padding(5.dp))
		}
		Spacer(Modifier.padding(5.dp))
	}
}

/**
 * Elemento de configuración para un carácter asociado a un modo.
 *
 * Permite al usuario cambiar el carácter asignado a un modo específico mediante un
 * `OutlinedTextField`. Valida que solo se ingrese un carácter y muestra error en caso contrario.
 *
 * @param modes Modo a configurar.
 */
@Composable
private fun SettingsItemForModes(modes: Modes) {
	val text = Modes.getModeName(modes)

	var newMode by remember { mutableStateOf("") }
	var isError by remember { mutableStateOf(false) }

	Card(
		modifier = Modifier
			.wrapContentWidth()
			.padding(horizontal = 10.dp, vertical = 5.dp),
		shape = RoundedCornerShape(10),
		colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary),
		elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
		border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary)
	) {
		Spacer(Modifier.padding(5.dp))

		Row(verticalAlignment = Alignment.CenterVertically) {
			Spacer(Modifier.padding(5.dp))

			Text(text = text, color = MaterialTheme.colorScheme.background)

			Spacer(Modifier.padding(10.dp))

			OutlinedTextField(
				value = newMode,
				onValueChange = {
					newMode = it

					if (it.length == 1) {
						val newChar = it[0]
						when (modes) {
							Modes.MANUAL -> Modes.MANUAL.char = newChar
							Modes.AUTOMATA -> Modes.AUTOMATA.char = newChar
						}
						isError = false
					} else {
						isError = true
					}
				},
				placeholder = {
					Text(
						text = "${modes.char}",
						color = MaterialTheme.colorScheme.primary
					)
				},
				singleLine = true,
				colors = OutlinedTextFieldDefaults.colors(
					unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
					focusedBorderColor = MaterialTheme.colorScheme.onSecondary,
					focusedTextColor = MaterialTheme.colorScheme.background,
					unfocusedLabelColor = MaterialTheme.colorScheme.background
				),
				modifier = Modifier.width(50.dp),
				isError = isError
			)
			Spacer(Modifier.padding(5.dp))
		}
		Spacer(Modifier.padding(5.dp))
	}
}

/**
 * Menú desplegable de configuración.
 *
 * Muestra sliders, modos y direcciones configurables dentro de un `DropdownMenu`.
 *
 * @param state Estado del menú: `true` si está abierto, `false` si está cerrado.
 * @param onStateChange Función que se ejecuta al cambiar el estado del menú.
 * @param setOfDirections Conjunto de direcciones a configurar.
 * @param setOfModes Conjunto de modos a configurar.
 * @param listOfSliders Lista de configuraciones de sliders a mostrar.
 */
@Composable
fun SettingsDropMenu(
	state: Boolean,
	onStateChange: (Boolean) -> Unit,
	setOfDirections: Set<Directions>,
	setOfModes: Set<Modes>,
	listOfSliders: List<SliderConfig>
) {
    DropdownMenu(
        expanded = state,
        onDismissRequest = { onStateChange(false) },
        modifier = Modifier
			.background(MaterialTheme.colorScheme.tertiary)
			.wrapContentSize()
    ) {
        listOfSliders.forEach { config ->
            SliderForConfiguration(
                value = config.value,
                onValueChange = config.onValueChange,
                valueRange = config.valueRange,
                ruta = config.ruta,
                typeForReset = config.typeForReset
            )
        }
        setOfModes.forEach { mode ->
            SettingsItemForModes(mode)
        }
        setOfDirections.forEach { direction ->
            SettingsItemForDirections(direction)
        }
    }
}
