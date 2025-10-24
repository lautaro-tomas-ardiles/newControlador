package com.example.newcontrolador.utilitis

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.newcontrolador.R
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newcontrolador.connection.data.Directions
import com.example.newcontrolador.connection.data.Modes
import com.example.newcontrolador.data.DataStoreViewModel
import com.example.newcontrolador.navigation.AppScreen

/**
 * Tarjeta de configuración para un carácter asociado a una dirección o modo.
 *
 * Muestra un `OutlinedTextField` que permite al usuario cambiar el carácter asignado.
 * Valida que solo se ingrese un carácter y actualiza el ViewModel en consecuencia.
 *
 * @param text Texto descriptivo para la configuración.
 * @param currentChar Carácter actualmente asignado.
 * @param isMode Indica si la configuración es para un modo (true) o una dirección (false).
 * @param modes Modo asociado (si aplica).
 * @param directions Dirección asociada (si aplica).
 * @param viewModel ViewModel para manejar el estado y las actualizaciones de los caracteres.
 */
@Composable
private fun SettingsCard(
	text: String,
	currentChar: Char,
	isMode: Boolean = false,
	modes: Modes? = null,
	directions: Directions,
	viewModel: DataStoreViewModel
) {
	var newChar by remember { mutableStateOf("") }

	Card(
		modifier = Modifier
			.wrapContentWidth()
			.padding(vertical = 5.dp),
		shape = RoundedCornerShape(10),
		colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onBackground),
		elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
		border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary)
	) {
		Spacer(Modifier.padding(5.dp))

		Row(verticalAlignment = Alignment.CenterVertically) {
			Spacer(Modifier.padding(5.dp))

			Text(text = text, color = MaterialTheme.colorScheme.tertiary)

			Spacer(Modifier.padding(10.dp))

			OutlinedTextField(
				value = newChar,
				onValueChange = {
					newChar = it

					if (it.length == 1) {
						if (isMode) {
							viewModel.setModeChar(modes ?: return@OutlinedTextField, it[0])
						} else {
							viewModel.setDirectionChar(directions, it[0])
						}

					}
				},
				placeholder = {
					Text(
						text = currentChar.toString(),
						color = MaterialTheme.colorScheme.primary
					)
				},
				singleLine = true,
				colors = OutlinedTextFieldDefaults.colors(
					unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
					focusedBorderColor = MaterialTheme.colorScheme.onSecondary,
					focusedTextColor = MaterialTheme.colorScheme.secondary,
					unfocusedLabelColor = MaterialTheme.colorScheme.onSecondary
				),
				modifier = Modifier.width(50.dp)
			)
			Spacer(Modifier.padding(5.dp))
		}
		Spacer(Modifier.padding(5.dp))
	}
}

/**
 * Elemento de configuración para un carácter asociado a una dirección.
 *
 * Permite al usuario cambiar el carácter asignado a la dirección indicada mediante un
 * `OutlinedTextField`. Valida que solo se ingrese un carácter y muestra error en caso contrario.
 *
 * @param directions Dirección a configurar.
 * @param viewModel ViewModel para manejar el estado y las actualizaciones de los caracteres.
 */
@Composable
fun SettingsItemForDirections(
	directions: Directions,
	viewModel: DataStoreViewModel
) {
	val text = Directions.getDirectionsName(directions)

	// Observa los valores actuales desde el ViewModel
	val directionsState by viewModel.directionChars.collectAsState()
	val currentChar = when (directions) {
		Directions.UP -> directionsState.upChar
		Directions.DOWN -> directionsState.downChar
		Directions.LEFT -> directionsState.leftChar
		Directions.RIGHT -> directionsState.rightChar
		Directions.UP_LEFT -> directionsState.upLeftChar
		Directions.UP_RIGHT -> directionsState.upRightChar
		Directions.DOWN_LEFT -> directionsState.downLeftChar
		Directions.DOWN_RIGHT -> directionsState.downRightChar
		Directions.STOP -> directionsState.stopChar
	}

	SettingsCard(
		text = text,
		currentChar = currentChar,
		directions = directions,
		viewModel = viewModel
	)
}

/**
 * Elemento de configuración para un carácter asociado a un modo.
 *
 * Permite al usuario cambiar el carácter asignado a un modo específico mediante un
 * `OutlinedTextField`. Valida que solo se ingrese un carácter y muestra error en caso contrario.
 *
 * @param modes Modo a configurar.
 * @param viewModel ViewModel para manejar el estado y las actualizaciones de los caracteres.
 */
@Composable
fun SettingsItemForModes(
	modes: Modes,
	viewModel: DataStoreViewModel
) {
	val text = Modes.getModeName(modes)

	// Observa los valores actuales desde el ViewModel
	val modesState by viewModel.modeChars.collectAsState()
	val currentChar = when (modes) {
		Modes.MANUAL -> modesState.modeManualChar
		Modes.AUTOMATA -> modesState.modeAutomataChar
	}

	SettingsCard(
		text = text,
		currentChar = currentChar,
		isMode = true,
		modes = modes,
		directions = Directions.STOP, // Valor por defecto, no se usa en este caso
		viewModel = viewModel,
	)
}