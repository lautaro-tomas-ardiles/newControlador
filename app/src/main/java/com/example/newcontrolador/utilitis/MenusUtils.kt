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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.newcontrolador.connection.Directions
import com.example.newcontrolador.connection.Modes
import com.example.newcontrolador.ui.theme.Black
import com.example.newcontrolador.ui.theme.Blue
import com.example.newcontrolador.ui.theme.DarkYellow
import com.example.newcontrolador.ui.theme.LightGreen
import com.example.newcontrolador.ui.theme.LightYellow

@Composable
fun DiagramaItem(
	text: String,
	onClick: () -> Unit
) {
	DropdownMenuItem(
		text = {
			Text(text = text, color = Black)
		},
		onClick = { onClick() }
	)
}

@Composable
fun SettingsItem(directionOrMode: Any) {
	val direction = directionOrMode as? Directions
	val mode = directionOrMode as? Modes

	val text = when (directionOrMode) {
		Directions.UP -> "Arriba"
		Directions.DOWN -> "Abajo"
		Directions.LEFT -> "Izquierda"
		Directions.RIGHT -> "Derecha"
		Directions.UP_LEFT -> "Arriba Izquierda"
		Directions.UP_RIGHT -> "Arriba Derecha"
		Directions.DOWN_LEFT -> "Abajo Izquierda"
		Directions.DOWN_RIGHT -> "Abajo Derecha"
		Directions.STOP -> "Detener"
		Modes.MANUAL -> "Control Manual"
		Modes.AUTOMATA -> "Automata"
		else -> "Desconocido"
	}
	var newDirection by remember { mutableStateOf("") }
	var isError by remember { mutableStateOf(false) }

	Card(
		modifier = Modifier
			.wrapContentWidth()
			.padding(horizontal = 10.dp, vertical = 5.dp),
		shape = RoundedCornerShape(10),
		colors = CardDefaults.cardColors(containerColor = LightGreen),
		elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
		border = BorderStroke(2.dp, LightYellow)
	) {
		Spacer(Modifier.padding(5.dp))

		Row(verticalAlignment = Alignment.CenterVertically) {
			Spacer(Modifier.padding(5.dp))

			Text(text = text, color = Black)

			Spacer(Modifier.padding(10.dp))

			OutlinedTextField(
				value = newDirection,
				onValueChange = {
					newDirection = it

					if (it.length == 1) {
						val newChar = it[0]
						when (directionOrMode) {
							Directions.UP -> Directions.UP.char = newChar
							Directions.DOWN -> Directions.DOWN.char = newChar
							Directions.LEFT -> Directions.LEFT.char = newChar
							Directions.RIGHT -> Directions.RIGHT.char = newChar
							Directions.UP_LEFT -> Directions.UP_LEFT.char = newChar
							Directions.UP_RIGHT -> Directions.UP_RIGHT.char = newChar
							Directions.DOWN_LEFT -> Directions.DOWN_LEFT.char = newChar
							Directions.DOWN_RIGHT -> Directions.DOWN_RIGHT.char = newChar
							Directions.STOP -> Directions.STOP.char = newChar
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
						text = "${direction?.char ?: (mode?.char ?: ' ')}",
						color = Blue
					)
				},
				singleLine = true,
				colors = OutlinedTextFieldDefaults.colors(
					unfocusedBorderColor = LightYellow,
					focusedBorderColor = DarkYellow,
					focusedTextColor = Black,
					unfocusedLabelColor = Black
				),
				modifier = Modifier.width(50.dp),
				isError = isError
			)
			Spacer(Modifier.padding(5.dp))
		}
		Spacer(Modifier.padding(5.dp))
	}
}

@Composable
fun ModeIcon(
	text: String,
	onClick: () -> Unit,
	stateOfItem: Boolean
) {
	DropdownMenuItem(
		text = {
			Text(text = text, color = Black)
		},
		onClick = { onClick() },
		trailingIcon = {
			RadioButton(
				selected = stateOfItem,
				onClick = { onClick() },
				colors = RadioButtonDefaults.colors(
					selectedColor = Blue,
					unselectedColor = Blue
				)
			)
		}
	)
}

@Composable
fun ModeDropMenu(
	state: Boolean,
	onStateChange: (Boolean) -> Unit,
	content: @Composable () -> Unit
) {
	DropdownMenu(
		expanded = state,
		onDismissRequest = { onStateChange(false) },
		modifier = Modifier
			.background(LightGreen)
			.wrapContentSize()
	) {
		content()
	}
}

@Composable
fun SettingsDropMenu(
	state: Boolean,
	onStateChange: (Boolean) -> Unit,
	content: @Composable () -> Unit
) {
	DropdownMenu(
		expanded = state,
		onDismissRequest = { onStateChange(false) },
		modifier = Modifier
			.background(LightGreen)
			.wrapContentSize()
	) {
		content()
	}
}

@Composable
fun DiagramaDropMenu(
	state: Boolean,
	onStateChange: (Boolean) -> Unit,
	content: @Composable () -> Unit
) {
	DropdownMenu(
		expanded = state,
		onDismissRequest = { onStateChange(false) },
		modifier = Modifier
			.background(LightGreen)
			.wrapContentSize()
	) {
		content()
	}
}