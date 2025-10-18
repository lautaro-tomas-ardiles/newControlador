package com.example.newcontrolador.screen

import android.bluetooth.BluetoothAdapter
import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newcontrolador.connection.BluetoothConnectionManager
import com.example.newcontrolador.connection.ConnectionViewModel
import com.example.newcontrolador.connection.WiFiConnectionManager
import com.example.newcontrolador.connection.data.ConfigButton
import com.example.newcontrolador.connection.data.ConfigDirections
import com.example.newcontrolador.connection.data.Directions
import com.example.newcontrolador.connection.data.Modes
import com.example.newcontrolador.data.DataStoreViewModel
import com.example.newcontrolador.utilitis.CustomSnackbar
import com.example.newcontrolador.utilitis.DirectionButton
import com.example.newcontrolador.utilitis.SetOrientation
import com.example.newcontrolador.utilitis.TopBarForMainPage
import kotlinx.coroutines.delay

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

@Composable
private fun GridButton(
	connectionManager: ConnectionViewModel,
	buttonConfig: ConfigButton,
	directionChars: ConfigDirections
) {
	val buttonHeight = buttonConfig.height.toInt()
	val buttonWidth = buttonConfig.width.toInt()
	val padding = buttonConfig.padding.toInt()

	var directionsPressed by remember { mutableStateOf(setOf<Directions>()) }
	var isPressed by remember { mutableStateOf(false) }

	// Efecto para enviar continuamente los caracteres mientras el botón esté presionado
	LaunchedEffect(isPressed, directionsPressed) {
		if (isPressed && directionsPressed.isNotEmpty()) {
			while (isPressed) {
				connectionManager.sendChar(
					Directions.charFromSet(
						directionsPressed,
						directionChars
					)
				)
				delay(50L) // cada 50 ms
			}
		} else {
			while (!isPressed) {
				connectionManager.sendChar(directionChars.stopChar)
				delay(50L)
			}
		}
	}

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
					directionsPressed = directionsPressed.toMutableSet().apply { remove(it) }

					if (directionsPressed.isEmpty()) {
						isPressed = false
					}
				},
				height = buttonHeight,
				width = buttonWidth
			)
			Spacer(Modifier.padding(padding.dp))

			DirectionButton(
				direction = Directions.DOWN,
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
				height = buttonHeight,
				width = buttonWidth
			)
		}

		Indicators(directionsPressed)

		Row {
			DirectionButton(
				direction = Directions.LEFT,
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
				height = buttonHeight,
				width = buttonWidth
			)
			Spacer(Modifier.padding(padding.dp))

			DirectionButton(
				direction = Directions.RIGHT,
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
				height = buttonHeight,
				width = buttonWidth
			)
		}
	}
}


@Composable
fun MainScreen(
	bluetoothAdapter: BluetoothAdapter,
	navController: NavController,
	viewModel: DataStoreViewModel
) {
	val snackbarHostState = remember { SnackbarHostState() }

	// Observa configuración desde DataStore
	val buttonConfig by viewModel.buttonConfig.collectAsState()
	val directions by viewModel.directionChars.collectAsState()
	val modes by viewModel.modeChars.collectAsState()

	var modeSelected by remember { mutableStateOf(Modes.MANUAL) }

	val bluetoothConnectionManager = remember { BluetoothConnectionManager() }
	val wifiManager = remember { WiFiConnectionManager() }
	val connectionManager = remember {
		ConnectionViewModel(
			bluetoothConnectionManager = bluetoothConnectionManager,
			wifiConnectionManager = wifiManager
		)
	}

	LaunchedEffect(modeSelected) {
		connectionManager.sendChar(
			when (modeSelected) {
				Modes.MANUAL -> modes.modeManualChar
				Modes.AUTOMATA -> modes.modeAutomataChar
			}
		)
	}
	LaunchedEffect(connectionManager.message) {
		connectionManager.message?.let {
			snackbarHostState.showSnackbar(it)
			connectionManager.cleanMessage()
		}
	}

	SetOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, LocalContext.current)

	Scaffold(
		topBar = {
			TopBarForMainPage(
				bluetoothAdapter = bluetoothAdapter,
				connectionManager = connectionManager,
				navController = navController,
				viewModel = viewModel,
				modeSelected = { modeSelected = it },
				configDirections = directions
			)
		},
		snackbarHost = {
			SnackbarHost(snackbarHostState) { data ->
				CustomSnackbar(data)
			}
		},
		containerColor = MaterialTheme.colorScheme.background
	) { padding ->
		Box(
			Modifier
				.padding(padding)
				.fillMaxSize(),
			contentAlignment = Alignment.Center
		) {
			GridButton(
				connectionManager = connectionManager,
				buttonConfig = buttonConfig,
				directionChars = directions
			)
		}
	}
}
