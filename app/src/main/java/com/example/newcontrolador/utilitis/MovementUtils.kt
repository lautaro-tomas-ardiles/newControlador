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
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.newcontrolador.connection.ConnectionViewModel
import com.example.newcontrolador.connection.data.Directions
import com.example.newcontrolador.connection.data.DirectionsConfig
import com.example.newcontrolador.data.DataStoreViewModel
import kotlinx.coroutines.delay
import androidx.compose.ui.unit.coerceAtMost
import androidx.navigation.compose.rememberNavController

class MovementUtils {
	private var maxButtonHeigthPercent = (1f / 2f)
	private var maxButtonWidthPercent = (1f / 4f)

	@Composable
	private fun Indicators(pressedButton: Set<Directions>) {
		val colorUp =
			if (Directions.UP in pressedButton)
				MaterialTheme.colorScheme.onSecondary
			else
				MaterialTheme.colorScheme.primary

		val colorDown =
			if (Directions.DOWN in pressedButton)
				MaterialTheme.colorScheme.onSecondary
			else
				MaterialTheme.colorScheme.primary

		val colorLeft =
			if (Directions.LEFT in pressedButton)
				MaterialTheme.colorScheme.onSecondary
			else
				MaterialTheme.colorScheme.primary

		val colorRight =
			if (Directions.RIGHT in pressedButton)
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
		direction: Directions,
		onPress: (Directions) -> Unit,
		onRelease: (Directions) -> Unit,
		buttonWidth: Dp,
		buttonHeight: Dp
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
		var directionsPressed by remember { mutableStateOf(setOf<Directions>()) }
		var isPressed by remember { mutableStateOf(false) }

		var aSidoEnviado = false

		val configButton by viewModel.buttonConfig.collectAsState()
		var buttonHeightPercent by remember { mutableFloatStateOf(configButton.height) }
		var buttonWidthPercent by remember { mutableFloatStateOf(configButton.width) }

		var padding by remember { mutableIntStateOf(configButton.padding.toInt()) }

		//enviar continuamente los caracteres mientras el botón esté presionado
		LaunchedEffect(isPressed, directionsPressed) {
			if (isPressed && directionsPressed.isNotEmpty()) {
				while (isPressed) {
					connectionManager.sendChar(
						Directions.charFromSet(directionsPressed, directionChars)
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
						direction = Directions.UP,
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

					Spacer(Modifier.height(padding.dp))

					DirectionButton(
						direction = Directions.DOWN,
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
						direction = Directions.LEFT,
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
						direction = Directions.RIGHT,
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

	@Composable
	fun GridButtonForPrev() {
		var directionsPressed by remember { mutableStateOf(setOf<Directions>()) }

		//val configButton by viewModel.buttonConfig.collectAsState()
		var buttonHeightPercent by remember { mutableFloatStateOf(9f) }
		var buttonWidthPercent by remember { mutableFloatStateOf(9f) }

		var padding by remember { mutableIntStateOf(100) }

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
						direction = Directions.UP,
						onPress = {
							directionsPressed = directionsPressed.toMutableSet().apply { add(it) }
						},
						onRelease = {
							directionsPressed = directionsPressed.toMutableSet().apply { remove(it) }
						},
						buttonWidth = buttonWidth,
						buttonHeight = buttonHeight
					)

					Spacer(Modifier.height(padding.dp))

					DirectionButton(
						direction = Directions.DOWN,
						onPress = {
							directionsPressed = directionsPressed.toMutableSet().apply { add(it) }
						},
						onRelease = {
							directionsPressed = directionsPressed.toMutableSet().apply { remove(it) }
						},
						buttonWidth = buttonWidth,
						buttonHeight = buttonHeight
					)
				}

				Indicators(directionsPressed)

				Row {
					DirectionButton(
						direction = Directions.LEFT,
						onPress = {
							directionsPressed = directionsPressed.toMutableSet().apply { add(it) }
						},
						onRelease = {
							directionsPressed = directionsPressed.toMutableSet().apply { remove(it) }
						},
						buttonWidth = buttonWidth,
						buttonHeight = buttonHeight
					)

					Spacer(Modifier.width(padding.dp))

					DirectionButton(
						direction = Directions.RIGHT,
						onPress = {
							directionsPressed = directionsPressed.toMutableSet().apply { add(it) }
						},
						onRelease = {
							directionsPressed = directionsPressed.toMutableSet().apply { remove(it) }
						},
						buttonWidth = buttonWidth,
						buttonHeight = buttonHeight
					)
				}
			}
		}
	}
}

@Preview(device = "spec:parent=pixel_5,orientation=landscape")
@Composable
private fun PreviewForTheSizeOfButtons() {
	val mevio = MovementUtils()

	MaterialTheme {
		Scaffold(
			topBar = {
				TopBar2("asdad", rememberNavController())
			},
			containerColor = MaterialTheme.colorScheme.background
		) { padding ->
			Box(
				Modifier
					.padding(padding)
					.fillMaxSize(),
				contentAlignment = Alignment.Center
			) {
				mevio.GridButtonForPrev()
			}
		}
	}
}