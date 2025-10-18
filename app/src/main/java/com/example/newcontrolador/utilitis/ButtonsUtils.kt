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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.newcontrolador.connection.data.Directions

/**
 * Botón direccional para control de movimiento.
 *
 * Muestra un botón con una flecha que indica la dirección especificada (arriba, abajo, izquierda o derecha).
 * Detecta la presión y liberación del botón para enviar eventos personalizados.
 *
 * @param direction Dirección del botón (UP, DOWN, LEFT o RIGHT).
 * @param onPress Función que se ejecuta cuando el botón es presionado.
 * @param onRelease Función que se ejecuta cuando el botón es liberado.
 * @param width Ancho del botón en dp.
 * @param height Alto del botón en dp.
 */
@Composable
fun DirectionButton(
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
			.background(MaterialTheme.colorScheme.onTertiary)
			.border(2.dp, MaterialTheme.colorScheme.secondary)
			.pointerInput(Unit) {
				detectTapGestures(
					onPress = {
						onPress(direction)
						tryAwaitRelease()
						onRelease(direction)
					}
				)
			},
		contentAlignment = Alignment.Center
	) {
		Icon(
			imageVector = arrowDirection,
			contentDescription = "Flecha de dirección $direction",
			modifier = Modifier
				.size(70.dp)
				.background(MaterialTheme.colorScheme.secondary, CircleShape),
			tint = MaterialTheme.colorScheme.background
		)
	}
}

/**
 * Botón icónico configurable.
 *
 * Muestra un botón con un ícono, que puede ser sólido o transparente, con o sin borde.
 * Puede mostrar un ícono especial de Bluetooth según el parámetro.
 *
 * @param onClick Acción que se ejecuta al presionar el botón.
 * @param isSolidColor Indica si el fondo debe ser sólido `true` o transparente `false`.
 * @param isBluetooth Si es `true`, muestra el ícono personalizado de Bluetooth.
 * @param border Si es `true`, muestra un borde alrededor del botón.
 * @param tintColor Color del ícono cuando no es Bluetooth.
 * @param imageVector Ícono a mostrar (por defecto, el ícono de ajustes).
 */
@Composable
fun IconsButtonsCustom(
	onClick: () -> Unit,
	isSolidColor: Boolean = false,
	isBluetooth: Boolean = false,
	border: Boolean = false,
	tintColor: Color = MaterialTheme.colorScheme.tertiary,
	imageVector: ImageVector = Icons.Default.Settings
) {
	IconButton(
		onClick = { onClick() },
		colors = IconButtonDefaults.iconButtonColors(
			containerColor =
				if (isSolidColor)
					MaterialTheme.colorScheme.secondary
				else
					Color.Transparent,
		),
		modifier = Modifier
			.size(45.dp)
			.border(
				width = 3.dp,
				color =
					if (border)
						MaterialTheme.colorScheme.secondary
					else
						Color.Transparent,
				shape = CircleShape
			)
	) {
		if (isBluetooth) {
			Icon(
				painter = painterResource(R.drawable.bluetooth),
				contentDescription = "Ícono de Bluetooth",
				tint = MaterialTheme.colorScheme.background
			)
		} else {
			Icon(
				imageVector = imageVector,
				contentDescription = "Ícono de acción",
				tint = tintColor,
				modifier = Modifier.size(30.dp)
			)
		}
	}
}

/**
 * Componente combinado de texto y botón.
 *
 * Muestra un texto acompañado de un botón icónico (por ejemplo, configuración o Bluetooth).
 *
 * @param text Texto que se muestra junto al botón.
 * @param imageVector Ícono del botón (por defecto, el ícono de opciones verticales).
 * @param isBluetooth Si es `true`, el botón adopta el estilo especial de Bluetooth.
 * @param onClick Acción que se ejecuta al presionar el botón.
 */
@Composable
fun TextAndButton(
	text: String,
	imageVector: ImageVector = Icons.Default.MoreVert,
	isBluetooth: Boolean = false,
	onClick: () -> Unit
) {
	Text(
		text = text,
		color = MaterialTheme.colorScheme.background,
		fontSize = MaterialTheme.typography.bodyMedium.fontSize
	)
	Spacer(modifier = Modifier.padding(3.dp))

	IconsButtonsCustom(
		onClick = { onClick() },
		border = !isBluetooth, // si es bluetooth no debe tener borde
		imageVector = imageVector,
		isBluetooth = isBluetooth,
		isSolidColor = isBluetooth // si es el boton de bluetooth tiene que ser solido
	)
}

/**
 * Botón simple con texto.
 *
 * Muestra un botón básico con fondo amarillo y texto negro.
 *
 * @param text Texto que se muestra en el botón.
 * @param onClick Acción que se ejecuta al presionar el botón.
 */
@Composable
fun SimpleButton(
	text: String,
	onClick: () -> Unit
) {
	Button(
		onClick = {	onClick() },
		colors = ButtonDefaults.buttonColors(
			containerColor = MaterialTheme.colorScheme.secondary
		)
	) {
		Text(
			text = text,
			color = MaterialTheme.colorScheme.background
		)
	}
}