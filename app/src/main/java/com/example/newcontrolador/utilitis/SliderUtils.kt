package com.example.newcontrolador.utilitis

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.example.newcontrolador.data.enums.SliderType

/**
 * Componente de slider para configuración de propiedades de botones u otros elementos.
 *
 * Muestra un slider junto con una imagen representativa, un valor numérico y un botón para
 * reiniciar el valor a su predeterminado según el tipo de configuración (ancho, alto o padding).
 *
 * @param value Valor actual del slider.
 * @param onValueChange Función que se ejecuta al cambiar el valor del slider.
 * @param textForReset Texto que se mostrará en el botón de reinicio. Por defecto `"Reset"`.
 * @param sliderType Un enum con todos los posibles tipos de slider disponible.
 * @param valueRange Rango permitido para el slider (`ClosedFloatingPointRange<Float>`).
 * @param ruta Imagen representativa que se mostrará al inicio del slider.
 */
@Composable
fun Slider(
	value: Float,
	onValueChange: (Float) -> Unit,
	textForReset: String = "Reset",
	sliderType: SliderType,
	valueRange: ClosedFloatingPointRange<Float>,
	ruta: Painter,
	buttonsUtils: ButtonsUtils,
	iconTint: Color = MaterialTheme.colorScheme.background
) {
	val rangoReal = valueRange.endInclusive - valueRange.start

	val setps = when (sliderType) {
		SliderType.PADDING -> {
			(rangoReal / 5 - 1).toInt()
		}
		SliderType.VELOCITY -> {
			(rangoReal / 10 - 1).toInt()
		}
		else -> {
			0
		}
	}

	Column(Modifier.padding(vertical = 5.dp)) {
		Row(verticalAlignment = Alignment.CenterVertically) {
			Spacer(Modifier.width(5.dp))

			Image(
				painter = ruta,
				contentDescription = null,
				colorFilter = ColorFilter.tint(iconTint)
			)
			Spacer(Modifier.width(5.dp))

			Slider(
				value = value,
				steps = setps,
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

			val width = if (sliderType in setOf(SliderType.WIDTH, SliderType.HEIGHT)) 50.dp else 40.dp
			Box(
				modifier = Modifier
					.height(40.dp)
					.width(width)
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
				when (sliderType) {
					SliderType.WIDTH, SliderType.HEIGHT -> {
						Text(
							text = "${(value * 100).toInt()}%",
							color = MaterialTheme.colorScheme.secondary
						)
					}
					else -> {
						Text(
						text = "${value.toInt()}",
						color = MaterialTheme.colorScheme.secondary
					)
					}
				}
			}
			Spacer(Modifier.width(5.dp))
		}
		Spacer(Modifier.height(5.dp))

		Row(verticalAlignment = Alignment.CenterVertically) {
			Spacer(Modifier.width(5.dp))

			buttonsUtils.Simple(textForReset) {
				when (sliderType) {
					SliderType.WIDTH -> onValueChange(0.8f)
					SliderType.HEIGHT -> onValueChange(0.9f)
					SliderType.PADDING -> onValueChange(0f)
					SliderType.VELOCITY -> onValueChange(50f)
					SliderType.ICON -> onValueChange(30f)
					SliderType.BUTTON -> onValueChange(45f)
				}
			}
		}
	}
}