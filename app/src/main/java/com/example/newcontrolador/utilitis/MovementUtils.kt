package com.example.newcontrolador.utilitis

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.newcontrolador.connection.ConnectionViewModel
import com.example.newcontrolador.data.enums.DirectionsEnum
import com.example.newcontrolador.data.configs.DirectionsConfig
import com.example.newcontrolador.storage.DataStoreViewModel
import kotlinx.coroutines.delay
import androidx.compose.ui.unit.coerceAtMost

class MovementUtils {
	private var maxButtonHeigthPercent = (1f / 2f)
	private var maxButtonWidthPercent = (1f / 4f)

	@Composable
	private fun Indicators(pressedButton: Set<DirectionsEnum>) {
		val colorUp =
			if (DirectionsEnum.UP in pressedButton)
				MaterialTheme.colorScheme.onSecondary
			else
				MaterialTheme.colorScheme.primary

		val colorDown =
			if (DirectionsEnum.DOWN in pressedButton)
				MaterialTheme.colorScheme.onSecondary
			else
				MaterialTheme.colorScheme.primary

		val colorLeft =
			if (DirectionsEnum.LEFT in pressedButton)
				MaterialTheme.colorScheme.onSecondary
			else
				MaterialTheme.colorScheme.primary

		val colorRight =
			if (DirectionsEnum.RIGHT in pressedButton)
				MaterialTheme.colorScheme.onSecondary
			else
				MaterialTheme.colorScheme.primary

		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.Center
		) {
			Icon(
				imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
				contentDescription = "indicador izquierdo",
				modifier = Modifier.size(65.dp),
				tint = colorLeft
			)
			Column {
				Icon(
					imageVector = Icons.Default.KeyboardArrowUp,
					contentDescription = "indicador superior",
					modifier = Modifier.size(65.dp),
					tint = colorUp
				)
				Spacer(Modifier.padding(25.dp))

				Icon(
					imageVector = Icons.Default.KeyboardArrowDown,
					contentDescription = "indicador inferior",
					modifier = Modifier.size(65.dp),
					tint = colorDown
				)
			}

			Icon(
				imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
				contentDescription = "indicador derecho",
				modifier = Modifier.size(65.dp),
				tint = colorRight
			)
		}
	}

	/**
	 * Botón direccional para control de movimiento.
	 *
	 * Muestra un botón con una flecha que indica la dirección especificada (arriba, abajo, izquierda o derecha).
	 * Detecta la presión y liberación del botón para enviar eventos personalizados.
	 *
	 * @param direction Dirección del botón (UP, DOWN, LEFT o RIGHT).
	 * @param onPress Función que se ejecuta cuando el botón es presionado.
	 * @param onRelease Función que se ejecuta cuando el botón es liberado.
	 */
	@Composable
	private fun DirectionButton(
		direction: DirectionsEnum,
		onPress: (DirectionsEnum) -> Unit,
		onRelease: (DirectionsEnum) -> Unit,
		buttonWidth: Dp,
		buttonHeight: Dp
	) {
		val arrowDirection = when (direction) {
			DirectionsEnum.UP -> Icons.Default.KeyboardArrowUp
			DirectionsEnum.DOWN -> Icons.Default.KeyboardArrowDown
			DirectionsEnum.LEFT -> Icons.AutoMirrored.Filled.KeyboardArrowLeft
			DirectionsEnum.RIGHT -> Icons.AutoMirrored.Filled.KeyboardArrowRight
			else -> Icons.Default.KeyboardArrowUp
		}

		Box(
			modifier = Modifier
				.width(buttonWidth)
				.height(buttonHeight)
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
					.size(buttonHeight * 0.6f)
					.background(MaterialTheme.colorScheme.secondary, CircleShape),
				tint = MaterialTheme.colorScheme.background
			)
		}

	}

	/**
	 * Grilla con botones e indicadores de movimiento
	 *
	 * @param connectionManager ViewModel para enviar los caracteres correspondientes a las direcciones
	 * @param directionChars Configuración de caracteres para cada dirección
	 * @param viewModel ViewModel para obtener la configuración de tamaño de los botones
	 */
	@Composable
	fun GridButton(
		connectionManager: ConnectionViewModel,
		directionChars: DirectionsConfig,
		viewModel: DataStoreViewModel
	) {
		var directionsPressed by remember { mutableStateOf(setOf<DirectionsEnum>()) }
		var isPressed by remember { mutableStateOf(false) }

		var aSidoEnviado = false

		val configButton by viewModel.movementConfig.collectAsState()
		var buttonHeightPercent by remember { mutableFloatStateOf(configButton.height) }
		var buttonWidthPercent by remember { mutableFloatStateOf(configButton.width) }

		var padding by remember { mutableIntStateOf(configButton.padding.toInt()) }

		//enviar continuamente los caracteres mientras el botón esté presionado
		LaunchedEffect(isPressed, directionsPressed) {
			if (isPressed && directionsPressed.isNotEmpty()) {
				while (isPressed) {
					connectionManager.sendChar(
						DirectionsEnum.charFromSet(directionsPressed, directionChars)
					)
					aSidoEnviado = !aSidoEnviado
					if (aSidoEnviado) delay(50L) else delay(1L)
				}
			} else {
				while (!isPressed) {
					connectionManager.sendChar(directionChars.stopChar)
					aSidoEnviado = !aSidoEnviado
					if (aSidoEnviado) delay(50L) else delay(1L)
				}
			}
		}

		BoxWithConstraints {
			val height = this.maxHeight
			val width = this.maxWidth

			val aviableHeight = height - padding.dp
			val aviableWidth = width - padding.dp

			val buttonHeight = (aviableHeight * (maxButtonHeigthPercent * buttonHeightPercent))
				.coerceAtMost(aviableHeight * maxButtonHeigthPercent)
			val buttonWidth = (aviableWidth * (maxButtonWidthPercent * buttonWidthPercent))
				.coerceAtMost(aviableWidth * maxButtonWidthPercent)

			Row(
				Modifier.fillMaxSize(),
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				Column {
					DirectionButton(
						direction = DirectionsEnum.UP,
						onPress = {
							directionsPressed = directionsPressed.toMutableSet().apply { add(it) }

							isPressed = true
						},
						onRelease = {
							directionsPressed = directionsPressed.toMutableSet().apply { remove(it) }

							if (directionsPressed.isEmpty()) {
								isPressed = false
							}
						},
						buttonWidth = buttonWidth,
						buttonHeight = buttonHeight
					)

					Spacer(Modifier.height(padding.dp))

					DirectionButton(
						direction = DirectionsEnum.DOWN,
						onPress = {
							directionsPressed =
								directionsPressed.toMutableSet().apply { add(it) }

							isPressed = true
						},
						onRelease = {
							directionsPressed =
								directionsPressed.toMutableSet().apply { remove(it) }

							if (directionsPressed.isEmpty()) {
								isPressed = false
							}
						},
						buttonWidth = buttonWidth,
						buttonHeight = buttonHeight
					)
				}

				Indicators(directionsPressed)

				Row {
					DirectionButton(
						direction = DirectionsEnum.LEFT,
						onPress = {
							directionsPressed =
								directionsPressed.toMutableSet().apply { add(it) }

							isPressed = true
						},
						onRelease = {
							directionsPressed =
								directionsPressed.toMutableSet().apply { remove(it) }

							if (directionsPressed.isEmpty()) {
								isPressed = false
							}
						},
						buttonWidth = buttonWidth,
						buttonHeight = buttonHeight
					)

					Spacer(Modifier.width(padding.dp))

					DirectionButton(
						direction = DirectionsEnum.RIGHT,
						onPress = {
							directionsPressed =
								directionsPressed.toMutableSet().apply { add(it) }

							isPressed = true
						},
						onRelease = {
							directionsPressed =
								directionsPressed.toMutableSet().apply { remove(it) }

							if (directionsPressed.isEmpty()) {
								isPressed = false
							}
						},
						buttonWidth = buttonWidth,
						buttonHeight = buttonHeight
					)
				}
			}
		}
	}
}