package com.example.newcontrolador.utilitis

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.ui.unit.dp
import com.example.newcontrolador.connection.data.Directions
import com.example.newcontrolador.connection.data.Modes
import com.example.newcontrolador.data.DataStoreViewModel

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

	var newDirection by remember { mutableStateOf(currentChar.toString()) }
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
						viewModel.setDirectionChar(directions, it[0])
						isError = false
					} else {
						isError = true
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

	var newMode by remember { mutableStateOf(currentChar.toString()) }
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

			Text(
				text = text,
				color = MaterialTheme.colorScheme.background
			)
			Spacer(modifier = Modifier.width(10.dp))

			OutlinedTextField(
				value = newMode,
				onValueChange = {
					newMode = it

					if (it.length == 1) {
						viewModel.setModeChar(modes, it[0])
						isError = false
					} else {
						isError = true
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
	viewModel: DataStoreViewModel,
	onStateChange: (Boolean) -> Unit,
	setOfDirections: Set<Directions>,
	setOfModes: Set<Modes>,
	listOfSliders: List<SliderConfig>,
	listOfThemes: List<ThemeConfig>
) {
	val scroll = rememberScrollState()

	DropdownMenu(
		expanded = state,
		onDismissRequest = { onStateChange(false) },
		modifier = Modifier
			.background(MaterialTheme.colorScheme.tertiary)
			.wrapContentSize()
	) {
		Row(
			modifier = Modifier
				.horizontalScroll(scroll)
				.padding(horizontal = 10.dp, vertical = 5.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			listOfThemes.forEach { themeConfig ->
				ThemeItem(
					isColorSelected = themeConfig.isColorSelected,
					onClick = { themeConfig.onClick() },
					theme = themeConfig.theme
				)
				Spacer(Modifier.width(5.dp))
			}
		}
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
			SettingsItemForModes(mode, viewModel)
		}
		setOfDirections.forEach { direction ->
			SettingsItemForDirections(direction, viewModel)
		}
	}
}
