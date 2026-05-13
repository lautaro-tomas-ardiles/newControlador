package com.example.newcontrolador.utilitis

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newcontrolador.ui.theme.NewControladorTheme

@Composable
fun Label(text: String, inverted: Boolean) {
	val primary = if (inverted) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
	val secondary = if (inverted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary

	Box(
		modifier = Modifier
			.background(
				color = primary,
				shape = RoundedCornerShape(20)
			)
			.border(
				width = 4.dp, color = secondary,
				shape = RoundedCornerShape(20)
			)
			.padding(horizontal = 18.dp, vertical = 8.dp)
			.wrapContentSize()
	) {
		Text(
			text = text,
			fontSize = 24.sp,
			color = secondary
		)
	}
}

@Preview
@Composable
private fun LabelPrev() {
	NewControladorTheme {
		Label(text = "Izq", true)
	}
}

