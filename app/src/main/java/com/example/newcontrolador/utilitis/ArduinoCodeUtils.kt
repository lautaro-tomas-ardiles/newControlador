package com.example.newcontrolador.utilitis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

/**
 * Componente que muestra código de Arduino con formato de bloque.
 *
 * Presenta el texto recibido dentro de un contenedor con fondo oscuro y tipografía monoespaciada,
 * imitando el estilo de un bloque de código. Permite seleccionar el texto para copiarlo fácilmente.
 *
 * @param text Código fuente de Arduino que se mostrará en el componente.
 */
@Composable
fun ArduinoCode(text: String) {
	Box(
		Modifier
			.background(MaterialTheme.colorScheme.onBackground)
			.padding(16.dp)
	) {
		SelectionContainer {
			Text(
				text = text.trimIndent(),
				color = MaterialTheme.colorScheme.tertiary,
				fontFamily = FontFamily.Monospace
			)
		}
	}
}