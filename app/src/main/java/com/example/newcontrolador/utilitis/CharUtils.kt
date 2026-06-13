package com.example.newcontrolador.utilitis

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import com.example.newcontrolador.connection.data.DirectionsEnum
import com.example.newcontrolador.connection.data.ModesEnum
import com.example.newcontrolador.data.DataStoreViewModel

/**
 * Tarjeta de configuración para un carácter asociado a una dirección o modo.
 *
 * Muestra un `OutlinedTextField` que permite al usuario cambiar el carácter asignado.
 * Válida que solo se ingrese un carácter y actualiza el ViewModel en consecuencia.
 *
 * @param text Texto descriptivo para la configuración.
 * @param currentChar Carácter actualmente asignado.
 * @param isMode Indica si la configuración es para un modo (true) o una dirección (false).
 * @param modesEnum Modo asociado (si aplica).
 * @param directionsEnum Dirección asociada (si aplica).
 * @param viewModel ViewModel para manejar el estado y las actualizaciones de los caracteres.
 */
@Composable
private fun SettingsCard(
	text: String,
	currentChar: Char,
	isMode: Boolean = false,
	modesEnum: ModesEnum? = null,
	directionsEnum: DirectionsEnum,
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

			Text(
				text = text,
				color = MaterialTheme.colorScheme.tertiary
			)
			Spacer(Modifier.padding(10.dp))

			OutlinedTextField(
				value = newChar,
				onValueChange = {
					newChar = it

					if (it.length == 1) {
						if (isMode) {
							viewModel.setModeChar(modesEnum ?: return@OutlinedTextField, it[0])
						} else {
							viewModel.setDirectionChar(directionsEnum, it[0])
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
 * `OutlinedTextField`. Válida que solo se ingrese un carácter y muestra error en caso contrario.
 *
 * @param directionsEnum Dirección a configurar.
 * @param viewModel ViewModel para manejar el estado y las actualizaciones de los caracteres.
 */
@Composable
fun SettingsItemForDirections(
	directionsEnum: DirectionsEnum,
	viewModel: DataStoreViewModel
) {
	val text = DirectionsEnum.getDirectionsName(directionsEnum)

	// Observa los valores actuales desde el ViewModel
	val directionsState by viewModel.directionChars.collectAsState()
	val currentChar = when (directionsEnum) {
		DirectionsEnum.UP -> directionsState.upChar
		DirectionsEnum.DOWN -> directionsState.downChar
		DirectionsEnum.LEFT -> directionsState.leftChar
		DirectionsEnum.RIGHT -> directionsState.rightChar
		DirectionsEnum.UP_LEFT -> directionsState.upLeftChar
		DirectionsEnum.UP_RIGHT -> directionsState.upRightChar
		DirectionsEnum.DOWN_LEFT -> directionsState.downLeftChar
		DirectionsEnum.DOWN_RIGHT -> directionsState.downRightChar
		DirectionsEnum.STOP -> directionsState.stopChar
	}

	SettingsCard(
		text = text,
		currentChar = currentChar,
		directionsEnum = directionsEnum,
		viewModel = viewModel
	)
}

/**
 * Elemento de configuración para un carácter asociado a un modo.
 *
 * Permite al usuario cambiar el carácter asignado a un modo específico mediante un
 * `OutlinedTextField`. Válida que solo se ingrese un carácter y muestra error en caso contrario.
 *
 * @param modesEnum Modo a configurar.
 * @param viewModel ViewModel para manejar el estado y las actualizaciones de los caracteres.
 */
@Composable
fun SettingsItemForModes(
	modesEnum: ModesEnum,
	viewModel: DataStoreViewModel
) {
	val text = ModesEnum.getModeName(modesEnum)

	// Observa los valores actuales desde el ViewModel
	val modesState by viewModel.modeChars.collectAsState()
	val currentChar = when (modesEnum) {
		ModesEnum.MANUAL -> modesState.modeManualChar
		ModesEnum.AUTOMATA -> modesState.modeAutomataChar
	}

	SettingsCard(
		text = text,
		currentChar = currentChar,
		isMode = true,
		modesEnum = modesEnum,
		directionsEnum = DirectionsEnum.STOP,
		viewModel = viewModel,
	)
}