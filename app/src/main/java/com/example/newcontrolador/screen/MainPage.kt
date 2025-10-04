package com.example.newcontrolador.screen

import android.bluetooth.BluetoothAdapter
import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import com.example.newcontrolador.connection.Directions
import com.example.newcontrolador.connection.Modes
import com.example.newcontrolador.connection.WiFiConnectionManager
import com.example.newcontrolador.ui.theme.Black
import com.example.newcontrolador.ui.theme.Blue
import com.example.newcontrolador.ui.theme.DarkYellow
import com.example.newcontrolador.utilitis.Button
import com.example.newcontrolador.utilitis.DefaultButtonSize
import com.example.newcontrolador.utilitis.SetOrientation
import com.example.newcontrolador.utilitis.TopBarForMainPage
import com.example.newcontrolador.utilitis.getDirectionChar

@Composable
fun Indicators(pressedButton: Set<Directions>) {
	val colorUp = if (Directions.UP in pressedButton) DarkYellow else Blue
	val colorDown = if (Directions.DOWN in pressedButton) DarkYellow else Blue
	val colorLeft = if (Directions.LEFT in pressedButton) DarkYellow else Blue
	val colorRight = if (Directions.RIGHT in pressedButton) DarkYellow else Blue

	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.Center
	) {
		Icon(
			imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
			contentDescription = null,
			modifier = Modifier.size(65.dp),
			tint = colorLeft
		)
		Column {
			Icon(
				imageVector = Icons.Default.KeyboardArrowUp,
				contentDescription = null,
				modifier = Modifier.size(65.dp),
				tint = colorUp
			)
			Spacer(Modifier.padding(25.dp))

			Icon(
				imageVector = Icons.Default.KeyboardArrowDown,
				contentDescription = null,
				modifier = Modifier.size(65.dp),
				tint = colorDown
			)
		}

		Icon(
			imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
			contentDescription = null,
			modifier = Modifier.size(65.dp),
			tint = colorRight
		)
	}
}

@Composable
fun GridButton(
	managerBluetooth: BluetoothConnectionManager,
	managerWiFi: WiFiConnectionManager,
	isBluetooth: Boolean,
	buttonHeight: Int,
	buttonWidth: Int,
	padding: Int
) {
	var directionsPressed by remember { mutableStateOf(setOf<Directions>()) }
	val context = LocalContext.current

	fun sendChar(char: Char) {
		if (isBluetooth) {
			managerBluetooth.sendChar(char, context)
		} else {
			managerWiFi.sendChar(char, context)
		}
	}

	Row(
		Modifier.fillMaxSize(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Column {
			Button(
				direction = Directions.UP,
				onPress = {
					directionsPressed = directionsPressed.toMutableSet().apply { add(it) }

					sendChar(getDirectionChar(directionsPressed))
				},
				onRelease = {
					directionsPressed = directionsPressed.toMutableSet().apply { remove(it) }

					sendChar(Directions.STOP.char)
				},
				height = buttonHeight,
				width = buttonWidth
			)
			Spacer(Modifier.padding(padding.dp))

			Button(
				direction = Directions.DOWN,
				onPress = {
					directionsPressed = directionsPressed.toMutableSet().apply { add(it) }

					sendChar(getDirectionChar(directionsPressed))
				},
				onRelease = {
					directionsPressed = directionsPressed.toMutableSet().apply { remove(it) }

					sendChar(Directions.STOP.char)
				},
				height = buttonHeight,
				width = buttonWidth
			)
		}

		Indicators(directionsPressed)

		Row {
			Button(
				direction = Directions.LEFT,
				onPress = {
					directionsPressed = directionsPressed.toMutableSet().apply { add(it) }

					sendChar(getDirectionChar(directionsPressed))
				},
				onRelease = {
					directionsPressed = directionsPressed.toMutableSet().apply { remove(it) }

					sendChar(Directions.STOP.char)
				},
				height = buttonHeight,
				width = buttonWidth
			)
			Spacer(Modifier.padding(padding.dp))

			Button(
				direction = Directions.RIGHT,
				onPress = {
					directionsPressed = directionsPressed.toMutableSet().apply { add(it) }

					sendChar(getDirectionChar(directionsPressed))
				},
				onRelease = {
					directionsPressed = directionsPressed.toMutableSet().apply { remove(it) }

					sendChar(Directions.STOP.char)
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
	navController: NavController
) {
	val defaultButtonSize = DefaultButtonSize()
	var buttonHeight by remember { mutableIntStateOf(defaultButtonSize.height.toInt()) }
	var buttonWidth by remember { mutableIntStateOf(defaultButtonSize.width.toInt()) }
	var paddings by remember { mutableIntStateOf(defaultButtonSize.padding.toInt()) }

	var bluetooth by remember { mutableStateOf(false) }

	var modeSelected by remember { mutableStateOf(Modes.MANUAL) }

	val bluetoothConnectionManager = remember { BluetoothConnectionManager() }
	val wifiManager = remember { WiFiConnectionManager() }

	val context = LocalContext.current

	LaunchedEffect(modeSelected, Unit) {
		bluetoothConnectionManager.sendChar(modeSelected.char, context)
	}
	SetOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, LocalContext.current)

	Scaffold(
		topBar = {
			TopBarForMainPage(
				bluetoothAdapter = bluetoothAdapter,
				wiFiConnectionManager = wifiManager,
				navController = navController,
				bluetoothConnectionManager = bluetoothConnectionManager,
				bluetoothEnable = { bluetooth = it },
				modeSelected = { modeSelected = it },
				buttonWidthValue = { buttonWidth = it },
				buttonHeightValue = { buttonHeight = it },
				paddingValues = { paddings = it }
			)
		},
		containerColor = Black
	) { padding ->
		Box(
			Modifier
				.padding(padding)
				.fillMaxSize(),
			contentAlignment = Alignment.Center
		) {
			GridButton(
				managerBluetooth = bluetoothConnectionManager,
				managerWiFi = wifiManager,
				isBluetooth = bluetooth,
				buttonHeight = buttonHeight,
				buttonWidth = buttonWidth,
				padding = paddings
			)
		}
	}
}