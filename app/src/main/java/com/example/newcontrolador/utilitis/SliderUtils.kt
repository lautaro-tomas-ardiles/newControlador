package com.example.newcontrolador.utilitis

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.example.newcontrolador.connection.data.Buttons
import com.example.newcontrolador.connection.data.DefaultButtonSize

/**
 * Componente de slider para configuración de propiedades de botones u otros elementos.
 *
 * Muestra un slider junto con una imagen representativa, un valor numérico y un botón para
 * reiniciar el valor a su predeterminado según el tipo de configuración (ancho, alto o padding).
 *
 * @param value Valor actual del slider.
 * @param onValueChange Función que se ejecuta al cambiar el valor del slider.
 * @param textForReset Texto que se mostrará en el botón de reinicio. Por defecto `"Reset"`.
 * @param typeForReset Tipo de valor que se debe reiniciar. Por defecto `Buttons.HEIGHT`.
 * @param valueRange Rango permitido para el slider (`ClosedFloatingPointRange<Float>`).
 * @param ruta Imagen representativa que se mostrará al inicio del slider.
 */
@Composable
fun SliderForConfiguration(
	value: Float,
	onValueChange: (Float) -> Unit,
	textForReset: String = "Reset",
	typeForReset: Buttons = Buttons.HEIGHT,
	valueRange: ClosedFloatingPointRange<Float>,
	ruta: Painter
) {
	val defaultButtonSize = DefaultButtonSize()

	Column {
		Row(verticalAlignment = Alignment.CenterVertically) {
			Spacer(Modifier.width(5.dp))

			Image(
				painter = ruta,
				contentDescription = null,
			)
			Spacer(Modifier.width(5.dp))

			Slider(
				value = value,
				onValueChange = { onValueChange(it) },
				valueRange = valueRange,
				colors = SliderDefaults.colors(
					thumbColor = MaterialTheme.colorScheme.primary,
					activeTrackColor = MaterialTheme.colorScheme.primary,
					inactiveTrackColor = MaterialTheme.colorScheme.secondary
				),
				modifier = Modifier.weight(1f)
			)
			Spacer(Modifier.width(5.dp))

			Box(
				modifier = Modifier
					.height(40.dp)
					.width(40.dp)
					.background(
						color = MaterialTheme.colorScheme.primary,
						shape = RoundedCornerShape(25)
					)
					.border(
						color = MaterialTheme.colorScheme.onSecondary,
						width = 2.dp,
						shape = RoundedCornerShape(25)
					),
				contentAlignment = Alignment.Center
			) {
				Text(
					text = "${value.toInt()}",
					color = MaterialTheme.colorScheme.secondary
				)
			}
			Spacer(Modifier.width(5.dp))
		}
		Spacer(Modifier.height(5.dp))

		Row(verticalAlignment = Alignment.CenterVertically) {
			Spacer(Modifier.width(5.dp))

			SimpleButton(textForReset) {
				when (typeForReset) {
					Buttons.WIDTH -> onValueChange(defaultButtonSize.width)
					Buttons.HEIGHT -> onValueChange(defaultButtonSize.height)
					Buttons.PADDING -> onValueChange(defaultButtonSize.padding)
				}
			}
		}
	}
}