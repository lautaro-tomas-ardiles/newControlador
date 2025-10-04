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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.newcontrolador.R
import androidx.compose.ui.unit.dp
import com.example.newcontrolador.ui.theme.Black
import com.example.newcontrolador.ui.theme.Blue
import com.example.newcontrolador.ui.theme.DarkBlue
import com.example.newcontrolador.ui.theme.DarkYellow
import com.example.newcontrolador.ui.theme.LightGreen
import com.example.newcontrolador.ui.theme.LightYellow
import com.example.newcontrolador.ui.theme.NewControladorTheme

@Composable
fun SliderForConfiguration(
	value: Float,
	onValueChange: (Float) -> Unit,
	textForReset: String = "Reset",
	typeForReset: ButtonSize = ButtonSize.HEIGHT,
	valueRange: ClosedFloatingPointRange<Float>,
	ruta: Painter
){
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
					thumbColor = Blue,
					activeTrackColor = Blue,
					inactiveTrackColor = LightYellow
				),
				modifier = Modifier.weight(1f)
			)
			Spacer(Modifier.width(5.dp))

			Box(
				modifier = Modifier
					.height(40.dp)
					.width(40.dp)
					.background(
						color = Blue,
						shape = RoundedCornerShape(25)
					)
					.border(
						color = DarkYellow,
						width = 2.dp,
						shape = RoundedCornerShape(25)
					),
				contentAlignment = Alignment.Center
			) {
				Text(
					text = "${value.toInt()}",
					color = LightYellow
				)
			}
			Spacer(Modifier.width(5.dp))
		}
		Spacer(Modifier.height(5.dp))

		Row(verticalAlignment = Alignment.CenterVertically) {
			Spacer(Modifier.width(5.dp))

			Button(
				onClick = {
					when (typeForReset) {
						ButtonSize.WIDTH -> onValueChange(defaultButtonSize.width)
						ButtonSize.HEIGHT -> onValueChange(defaultButtonSize.height)
						ButtonSize.PADDING -> onValueChange(defaultButtonSize.padding)
					}
				},
				colors = ButtonDefaults.buttonColors(
					containerColor = LightYellow
				),
			) {
				Text(
					text = textForReset,
					color = Black
				)
			}
		}
	}
}

@Preview
@Composable
fun PreviewSliderForConfiguration() {
	var buttonHeight by remember { mutableFloatStateOf(150f) }
	var buttonHeight2 by remember { mutableFloatStateOf(165f) }
	var buttonHeight3 by remember { mutableFloatStateOf(0f) }

	NewControladorTheme {
		Column (modifier = Modifier.background(color = LightGreen)) {
			SliderForConfiguration(
				value = buttonHeight,
				onValueChange = { buttonHeight = it },
				valueRange = 100f..300f,
				ruta = painterResource(id = R.drawable.group_1)
			)
			SliderForConfiguration(
				value = buttonHeight2,
				onValueChange = { buttonHeight2 = it },
				valueRange = 100f..300f,
				ruta = painterResource(id = R.drawable.group_3)
			)
			SliderForConfiguration(
				value = buttonHeight3,
				onValueChange = { buttonHeight3 = it },
				valueRange = 0f..50f,
				ruta = painterResource(id = R.drawable.group_4__1_)
			)
		}
	}
}