package com.example.newcontrolador.utilitis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Elemento individual de un menú desplegable para diagramas.
 *
 * Muestra un texto dentro de un `DropdownMenuItem` y ejecuta una acción cuando se presiona.
 *
 * @param text Texto que se mostrará en el elemento del menú.
 * @param onClick Acción que se ejecuta al seleccionar el elemento.
 */
@Composable
fun DiagramaItem(
	text: String,
	onClick: () -> Unit
) {
	DropdownMenuItem(
		text = {
			Text(text = text, color = MaterialTheme.colorScheme.background)
		},
		onClick = { onClick() }
	)
}

/**
 * Menú desplegable para diagramas.
 *
 * Envuelve un conjunto de elementos (`content`) dentro de un `DropdownMenu` con estilo personalizado.
 * Permite mostrar u ocultar el menú según el estado (`state`) y notificar cambios de estado.
 *
 * @param state Indica si el menú está expandido (`true`) o cerrado (`false`).
 * @param onStateChange Función que se ejecuta al cambiar el estado del menú (por ejemplo, al cerrar).
 * @param content Contenido del menú: elementos individuales como [DiagramaItem].
 */
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
			.background(MaterialTheme.colorScheme.tertiary)
			.wrapContentSize()
	) {
		content()
	}
}