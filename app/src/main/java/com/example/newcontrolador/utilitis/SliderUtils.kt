package com.example.newcontrolador.utilitis

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.example.newcontrolador.ui.theme.NewControladorTheme
import com.example.newcontrolador.connection.data.ButtonsEnum

/**
 * Despues deberia poner todo o la mayoria en una clase
 */
@Composable
private fun Slider(
	isShoulder: Boolean,
	inverted: Boolean,
	onSendText: (String) -> Unit
) {
	val density = LocalDensity.current

	val screenHeightPx = with(density) {
		LocalConfiguration.current.screenHeightDp.dp.toPx()
	}

	val topInset = WindowInsets.statusBars.getTop(density)
	val bottomInset = WindowInsets.navigationBars.getBottom(density)

	//* altura utilisable de la pantalla
	val usableHeightDp = with(density) {
		(screenHeightPx - topInset - bottomInset).toDp()
	}

	val valueRange = if (isShoulder) 0f..180f else 0f..90f
	var value by remember { mutableFloatStateOf(0f) }

	val valueForLabel = "%03d".format(value.toInt())

	val (colorPrimary, colorSecondary) =
		if (inverted) {
			MaterialTheme.colorScheme.secondary to MaterialTheme.colorScheme.primary
		} else {
			MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.secondary
		}
	Column (
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier.height(usableHeightDp / 4)
	) {
		Slider(
			value = value,
			onValueChange = {
				val intValue = it.toInt()

				if (intValue != value.toInt()) {
					val prefix = if (isShoulder) 'H' else 'C'
					val suffix = if (inverted) '1' else '2'
					onSendText("+$prefix$suffix$valueForLabel")
				}
				value = it
			},
			valueRange = valueRange,
			colors =
				SliderDefaults.colors(
					thumbColor = colorPrimary,
					activeTrackColor = colorPrimary,
					inactiveTrackColor = colorSecondary
				),
			modifier = Modifier
				.weight(1f)
				.graphicsLayer {
					rotationZ = 90f
					transformOrigin = TransformOrigin(0f, 0f)
				}
				.layout { measurable, constraints ->
					val placeable = measurable.measure(
						Constraints(
							minWidth = constraints.minHeight,
							maxWidth = constraints.maxHeight,
							minHeight = constraints.minWidth,
							maxHeight = constraints.maxWidth,
						)
					)
					layout(placeable.height, placeable.width) {
						placeable.place(0, -placeable.height)
					}
				}
		)
		//if (intValue < 10) "00$intValue" else if (intValue < 100) "0$intValue" else "$intValue"

		Spacer(Modifier.padding(5.dp))

		Label(valueForLabel, inverted)

		Spacer(Modifier.padding(10.dp))
	}
}

@Preview(device = "spec:width=411dp,height=891dp")
@Composable
private fun SliderPrev() {
	NewControladorTheme {
		Row {
			Slider(isShoulder = true, inverted = false) {/*TODO: aca nunca va haber nada*/}
			Slider(isShoulder = false, inverted = true) {/*TODO: aca nunca va haber nada*/}
		}
	}
}

@Composable
fun JointSliders(isShoulder: Boolean, onSendText: (String) -> Unit) {
	val text = if (isShoulder) "Hombros" else "Codos"

	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween,
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 10.dp)
	) {
		Slider(
			isShoulder = isShoulder,
			inverted = false,
			onSendText = onSendText
		)
		Label(
			text = text,
			inverted = true
		)
		Slider(
			isShoulder = isShoulder,
			inverted = true,
			onSendText = onSendText
		)
	}
}

class SliderForConfiguration {
	@Composable
	fun Slider(
		value: Float,
		onValueChange: (Float) -> Unit,
		textForReset: String = "Reset",
		typeForReset: ButtonsEnum? = ButtonsEnum.HEIGHT,
		valueRange: ClosedFloatingPointRange<Float>,
		ruta: Painter
	) {
		val button = Buttons()
		val setps =
			if (typeForReset == null) {
				((valueRange.endInclusive - valueRange.start) / 5).toInt() - 1
			} else { 0 }

		Column(Modifier.padding(vertical = 5.dp)) {
			Row(verticalAlignment = Alignment.CenterVertically) {
				Spacer(Modifier.width(5.dp))

				Image(
					painter = ruta,
					contentDescription = null
				)
				Spacer(Modifier.width(5.dp))

				Slider(
					value = value,
					onValueChange = { onValueChange(it) },
					valueRange = valueRange,
					steps = setps,
					colors = SliderDefaults.colors(
						thumbColor = MaterialTheme.colorScheme.primary,
						activeTrackColor = MaterialTheme.colorScheme.primary,
						inactiveTrackColor = MaterialTheme.colorScheme.secondary
					),
					modifier = Modifier.weight(1f)
				)
				Spacer(Modifier.width(5.dp))

				val width = if (typeForReset != null) 50.dp else 40.dp
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
					if (typeForReset != null) {
						Text(
							text = "${(value * 100).toInt()}%",
							color = MaterialTheme.colorScheme.secondary
						)
					} else {
						Text(
							text = "${value.toInt()}",
							color = MaterialTheme.colorScheme.secondary
						)
					}
				}
				Spacer(Modifier.width(5.dp))
			}
			Spacer(Modifier.height(5.dp))

			Row(verticalAlignment = Alignment.CenterVertically) {
				Spacer(Modifier.width(5.dp))

				button.Simple(textForReset) {
					when (typeForReset) {
						ButtonsEnum.WIDTH -> onValueChange(0.95f)
						ButtonsEnum.HEIGHT -> onValueChange(0.75f)
						else -> onValueChange(50f)
					}
				}
			}
		}
	}
}

