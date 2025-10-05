package com.example.newcontrolador.utilitis

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.newcontrolador.R
import com.example.newcontrolador.connection.Directions
import com.example.newcontrolador.ui.theme.Black
import com.example.newcontrolador.ui.theme.DarkGreen
import com.example.newcontrolador.ui.theme.LightGreen
import com.example.newcontrolador.ui.theme.LightYellow

@Composable
fun Button(
	direction: Directions,
	onPress: (Directions) -> Unit,
	onRelease: (Directions) -> Unit,
	width: Int,
	height: Int
) {
	val arrowDirection = when (direction) {
		Directions.UP -> Icons.Default.KeyboardArrowUp
		Directions.DOWN -> Icons.Default.KeyboardArrowDown
		Directions.LEFT -> Icons.AutoMirrored.Filled.KeyboardArrowLeft
		Directions.RIGHT -> Icons.AutoMirrored.Filled.KeyboardArrowRight
		else -> Icons.Default.KeyboardArrowUp
	}

	Box(
		modifier = Modifier
			.height(height.dp)
			.width(width.dp)
			.background(DarkGreen)
			.border(2.dp, LightYellow)
			.pointerInput(Unit) {
				detectTapGestures(
					onPress = {
						onPress(direction)
						tryAwaitRelease()
						onRelease(direction)
					},
				)
			},
		contentAlignment = Alignment.Center
	) {
		Icon(
			imageVector = arrowDirection,
			contentDescription = null,
			modifier = Modifier
				.size(70.dp)
				.background(LightYellow, CircleShape),
			tint = Black
		)
	}
}

@Composable
fun IconsButtons(
	onClick: () -> Unit,
	isSolidColor: Boolean = false,
	isBluetooth: Boolean = false,
	border: Boolean = false,
	tintColor: Color = LightGreen,
	imageVector: ImageVector = Icons.Default.Settings
) {
	IconButton(
		onClick = { onClick() },
		colors = IconButtonDefaults.iconButtonColors(
			containerColor = if (isSolidColor) LightYellow else Color.Transparent,
		),
		modifier = Modifier
			.size(45.dp)
			.border(
				width = 3.dp,
				color = if (border) LightYellow else Color.Transparent,
				shape = CircleShape
			)
	) {
		if (isBluetooth) {
			Icon(
				painter = painterResource(R.drawable.group_11),
				contentDescription = null,
				tint = Black
			)
		} else {
			Icon(
				imageVector = imageVector,
				contentDescription = null,
				tint = tintColor,
				modifier = Modifier.size(30.dp)
			)
		}
	}
}

@Composable
fun TextAndButton(
	text: String,
	imageVector: ImageVector = Icons.Default.MoreVert,
	isBluetooth: Boolean = false,
	onClick: () -> Unit
) {
	Text(
		text = text,
		color = Black,
		fontSize = MaterialTheme.typography.bodyMedium.fontSize
	)
	Spacer(modifier = Modifier.padding(3.dp))

	IconsButtons(
		onClick = { onClick() },
		border = !isBluetooth, // si es bluetooth no debe tener borde
		imageVector = imageVector,
		isBluetooth = isBluetooth,
		isSolidColor = isBluetooth // si es el boton de bluetooth tiene que ser solido
	)
}