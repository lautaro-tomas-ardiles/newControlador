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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.newcontrolador.connection.data.Directions
import com.example.newcontrolador.connection.data.Modes
import com.example.newcontrolador.connection.WiFiConnectionManager
import com.example.newcontrolador.connection.data.DefaultButtonSize
import com.example.newcontrolador.ui.theme.ThemeType
import com.example.newcontrolador.utilitis.DirectionButton
import com.example.newcontrolador.utilitis.CustomSnackbar
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
	conectionManager: ConnectionViewModel,
	buttonHeight: Int,
	buttonWidth: Int,
	padding: Int
) {
	var directionsPressed by remember { mutableStateOf(setOf<Directions>()) }
	var isPressed by remember { mutableStateOf(false) }

    // Este efecto se ejecuta mientras el botón esté presionado
    LaunchedEffect(isPressed, directionsPressed) {
        if (isPressed && directionsPressed.isNotEmpty()) {
            while (isPressed) {
                conectionManager.sendChar(Directions.charFromSet(directionsPressed))
                delay(50L) // cada 50 ms
            }
        } else {
			while (!isPressed) {
				conectionManager.sendChar(Directions.STOP.char)
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
	selectThemeType: (ThemeType) -> Unit
) {
	val defaultButtonSize = DefaultButtonSize()

	var buttonHeight by remember { mutableIntStateOf(defaultButtonSize.height.toInt()) }
	var buttonWidth by remember { mutableIntStateOf(defaultButtonSize.width.toInt()) }
	var paddings by remember { mutableIntStateOf(defaultButtonSize.padding.toInt()) }

	val snackbarHostState = remember { SnackbarHostState() }

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
		connectionManager.sendChar(modeSelected.char)
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
				modeSelected = { modeSelected = it },
				buttonWidthValue = { buttonWidth = it },
				buttonHeightValue = { buttonHeight = it },
				paddingValues = { paddings = it },
				themeType = { selectThemeType(it) }
			)
		},
		snackbarHost = {
			SnackbarHost(
				snackbarHostState,
				modifier = Modifier
					.wrapContentWidth()
					.wrapContentHeight()
			) { data ->
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
				conectionManager = connectionManager,
				buttonHeight = buttonHeight,
				buttonWidth = buttonWidth,
				padding = paddings
			)
		}
	}
}