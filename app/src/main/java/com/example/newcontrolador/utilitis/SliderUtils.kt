package com.example.newcontrolador.utilitis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.example.newcontrolador.ui.theme.NewControladorTheme

@Composable
private fun Slider(isShoulder: Boolean, inverted: Boolean) {
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
			onValueChange = { value = it },
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
		Spacer(Modifier.padding(5.dp))
		Label("${value.toInt()}", inverted)
		Spacer(Modifier.padding(10.dp))
	}
}

@Preview(device = "spec:width=411dp,height=891dp")
@Composable
private fun SliderPrev() {
	NewControladorTheme {
		Row {
			Slider(isShoulder = true, inverted = false)
			Slider(isShoulder = false, inverted = true)
		}
	}
}

@Composable
fun JointSliders(isShoulder: Boolean) {
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
			inverted = false
		)
		Label(
			text = text,
			inverted = true
		)
		Slider(
			isShoulder = isShoulder,
			inverted = true
		)
	}
}

