package com.example.newcontrolador.utilitis

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.newcontrolador.connection.ConnectionViewModel
import com.example.newcontrolador.connection.data.Directions
import com.example.newcontrolador.connection.data.DirectionsConfig
import kotlinx.coroutines.delay

class MovementButtons {
	private var maxButtonHeigthPercent = (1f / 2f)
	private var maxButtonWidthPercent = (1f / 3f)

	private var buttonHeigthPercent = maxButtonHeigthPercent * 0.75f
	private var buttonWidthPercent = maxButtonWidthPercent * 0.95f

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

	@Composable
	fun GridButton(
		connectionManager: ConnectionViewModel,
		directionChars: DirectionsConfig
	) {
		var directionsPressed by remember { mutableStateOf(setOf<Directions>()) }
		var isPressed by remember { mutableStateOf(false) }

		//enviar continuamente los caracteres mientras el botón esté presionado
		LaunchedEffect(isPressed, directionsPressed) {
			if (isPressed && directionsPressed.isNotEmpty()) {
				while (isPressed) {
					connectionManager.sendChar(
						Directions.charFromSet(directionsPressed, directionChars)
					)
					delay(50L)
				}
			} else {
				while (!isPressed) {
					connectionManager.sendChar(directionChars.stopChar)
					delay(50L)
				}
			}
		}

		BoxWithConstraints {
			val height = this.maxHeight
			val width = this.maxWidth

			val buttonHeight = (height * buttonHeigthPercent)
				.coerceAtMost(height * maxButtonHeigthPercent)
			val buttonWidth = (width * buttonWidthPercent)
				.coerceAtMost(width * maxButtonWidthPercent)

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

					DirectionButton(
						direction = Directions.DOWN,
						onPress = {
							directionsPressed = directionsPressed.toMutableSet().apply { add(it) }

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

				Row {
					DirectionButton(
						direction = Directions.LEFT,
						onPress = {
							directionsPressed = directionsPressed.toMutableSet().apply { add(it) }

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

					DirectionButton(
						direction = Directions.RIGHT,
						onPress = {
							directionsPressed = directionsPressed.toMutableSet().apply { add(it) }

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
	fun GridButtonA(
		//* connectionManager: ConnectionViewModel,
		//* directionChars: DirectionsConfig para despues
	) {
		var directionsPressed by remember { mutableStateOf(setOf<Directions>()) }
		var isPressed by remember { mutableStateOf(false) }

		//enviar continuamente los caracteres mientras el botón esté presionado
		LaunchedEffect(isPressed, directionsPressed) {
			if (isPressed && directionsPressed.isNotEmpty()) {
				while (isPressed) {
//					connectionManager.sendChar(
//						Directions.charFromSet(
//							directionsPressed,
//							directionChars
//						)
//					)
					delay(50L)
				}
			} else {
				while (!isPressed) {
//					connectionManager.sendChar(directionChars.stopChar)
//					delay(50L)
				}
			}
		}

		BoxWithConstraints {
			val height = this.maxHeight
			val width = this.maxWidth

			val buttonHeight = (height * buttonHeigthPercent)
				.coerceAtMost(height * maxButtonHeigthPercent)
			val buttonWidth = (width * buttonWidthPercent)
				.coerceAtMost(width * maxButtonWidthPercent)

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
					//Spacer(Modifier.height(10.dp))

					DirectionButton(
						direction = Directions.DOWN,
						onPress = {
							directionsPressed = directionsPressed.toMutableSet().apply { add(it) }

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

				Row {
					DirectionButton(
						direction = Directions.LEFT,
						onPress = {
							directionsPressed = directionsPressed.toMutableSet().apply { add(it) }

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
					//Spacer(Modifier.width(10.dp))

					DirectionButton(
						direction = Directions.RIGHT,
						onPress = {
							directionsPressed = directionsPressed.toMutableSet().apply { add(it) }

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

class Buttons {
	private val shape = RoundedCornerShape(24)
	private val buttonSize = Modifier.size(38.dp)
	private val iconSize = Modifier.size(28.dp)

	@Composable
	fun Painter(
		//onClick: () -> Unit,
		painter: Painter,
		inverted: Boolean,
		connectionManager: ConnectionViewModel,
		bluetoothAdapter: BluetoothAdapter,
		contentDescription: String
	) {
		var menuDevicesState by remember { mutableStateOf(false) }
		val context = LocalContext.current

		val primary =
			if (inverted) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
		val secondary =
			if (inverted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary

		val hasPermission =
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
				ActivityCompat.checkSelfPermission(
					context,
					Manifest.permission.BLUETOOTH_CONNECT
				) == PackageManager.PERMISSION_GRANTED
			} else {
				ActivityCompat.checkSelfPermission(
					context,
					Manifest.permission.BLUETOOTH
				) == PackageManager.PERMISSION_GRANTED
			}

		val pairedDevices: Set<BluetoothDevice>

		if (hasPermission) {
			pairedDevices = bluetoothAdapter.bondedDevices
		} else {
			pairedDevices = setOf()
		}

		IconButton(
			onClick = {
				menuDevicesState = !menuDevicesState
			},
			shape = shape,
			modifier = buttonSize,
			colors = IconButtonDefaults.iconButtonColors(
				containerColor = primary,
				contentColor = secondary
			)
		) {
			Icon(
				painter = painter,
				contentDescription = contentDescription,
				tint = secondary,
				modifier = iconSize
			)
		}
		BluetoothDropMenu(
			state = menuDevicesState,
			onStateChange = { menuDevicesState = it },
			setOfDevices = pairedDevices,
			connectionManager = connectionManager,
			context = LocalContext.current
		)
	}

	@Composable
	fun ImageVector(
		onClick: () -> Unit,
		inverted: Boolean,
		imageVector: ImageVector,
		contentDescription: String
	) {
		val primary = if (inverted) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
		val secondary = if (inverted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary

		IconButton(
			onClick = { onClick() },
			shape = shape,
			modifier = buttonSize,
			colors = IconButtonDefaults.iconButtonColors(
				containerColor = primary,
				contentColor = secondary
			)
		) {
			Icon(
				imageVector = imageVector,
				contentDescription = contentDescription,
				tint = secondary,
				modifier = iconSize
			)
		}
	}

	@Composable
	fun Toggle(
		onClick: () -> Unit,
		onSecondClick: () -> Unit,
		isPressed: Boolean,
		onChangepressed: (Boolean) -> Unit,
		imageVector: ImageVector,
		painter: Painter,
		contentDescription: String
	) {
		val primary = MaterialTheme.colorScheme.primary
		val tertiary = MaterialTheme.colorScheme.tertiary

		IconButton(
			onClick = {
				if (!isPressed) onClick() else onSecondClick()
				onChangepressed(!isPressed)
			},
			shape = shape,
			modifier = buttonSize,
			colors = IconButtonDefaults.iconButtonColors(
				containerColor = if (isPressed) tertiary else primary,
				contentColor = if (isPressed) primary else tertiary
			)
		) {
			if (isPressed) {
				Icon(
					painter = painter,
					contentDescription = contentDescription
				)
			} else {
				Icon(
					imageVector = imageVector,
					contentDescription = contentDescription,
					modifier = iconSize
				)
			}
		}
	}
}