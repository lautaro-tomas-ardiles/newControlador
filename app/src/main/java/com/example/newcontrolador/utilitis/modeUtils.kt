package com.example.newcontrolador.utilitis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.newcontrolador.connection.data.Modes

/**
 * Elemento individual de un menú desplegable para seleccionar un modo.
 *
 * Muestra un texto con un `RadioButton` que indica si el elemento está seleccionado.
 * Ejecuta una acción cuando se presiona el elemento o el botón de selección.
 *
 * @param text Texto que se mostrará en el elemento del menú.
 * @param onClick Acción que se ejecuta al seleccionar el elemento.
 * @param stateOfItem Indica si el elemento está actualmente seleccionado (`true`) o no (`false`).
 */
@Composable
private fun ModeItem(
	text: String,
	onClick: () -> Unit,
	stateOfItem: Boolean
) {
	DropdownMenuItem(
		text = {
			Text(text = text, color = MaterialTheme.colorScheme.background)
		},
		onClick = { onClick() },
		trailingIcon = {
			RadioButton(
				selected = stateOfItem,
				onClick = { onClick() },
				colors = RadioButtonDefaults.colors(
					selectedColor = MaterialTheme.colorScheme.primary,
					unselectedColor = MaterialTheme.colorScheme.primary
				)
			)
		}
	)
}

/**
 * Menú desplegable para seleccionar un modo.
 *
 * Muestra todos los modos disponibles en `setOfModes` como elementos individuales con `ModeItem`.
 * Permite seleccionar un modo y notificar cambios de estado al menú.
 *
 * @param state Indica si el menú está expandido (`true`) o cerrado (`false`).
 * @param onStateChange Función que se ejecuta al cambiar el estado del menú (por ejemplo, al cerrarlo).
 * @param setOfModes Conjunto de modos disponibles para selección.
 * @param onClick Función que se ejecuta al seleccionar un modo específico.
 * @param modeSelect Modo actualmente seleccionado.
 */
@Composable
fun ModeDropMenu(
	state: Boolean ,
	onStateChange: (Boolean) -> Unit ,
	setOfModes: Set<Modes> ,
	onClick: (Modes) -> Unit ,
	modeSelect: Modes
) {
	DropdownMenu(
		expanded = state,
		onDismissRequest = { onStateChange(false) },
		modifier = Modifier
			.background(MaterialTheme.colorScheme.tertiary)
			.wrapContentSize()
	) {
		setOfModes.forEach { modes ->
			ModeItem(
				text = Modes.getModeName(modes),
				onClick = { onClick(modes) },
				stateOfItem = modeSelect == modes
			)
		}
	}
}