@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.newcontrolador.utilitis

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.example.newcontrolador.R
import com.example.newcontrolador.connection.ConnectionViewModel
import com.example.newcontrolador.connection.Directions
import com.example.newcontrolador.connection.Modes
import com.example.newcontrolador.navigation.AppScreen
import com.example.newcontrolador.ui.theme.Black
import com.example.newcontrolador.ui.theme.Blue

@Composable
fun TopBar2(text: String, navController: NavController) {
	TopAppBar(
		title = {
			Text(
				text = text,
				color = Black
			)
		},
		navigationIcon = {
			Row {
				IconsButtons(
					onClick = {
						navController.navigate(AppScreen.MainPage.route)
					},
					imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
					tintColor = Black
				)
			}
		},
		colors = TopAppBarDefaults.topAppBarColors(
			containerColor = Blue
		)
	)
}

@Composable
private fun TopBarForMainPageStart(
	onBluetoothChange: (Boolean) -> Unit,
	connectionManager: ConnectionViewModel,
	bluetoothAdapter: BluetoothAdapter
) {
	var ip by remember { mutableStateOf("") }

	var menuDevicesState by remember { mutableStateOf(false) }

	var bluetooth by remember { mutableStateOf(true) }

	val context = LocalContext.current

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

	Row(
		horizontalArrangement = Arrangement.Start,
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.fillMaxWidth()
			.padding(start = 20.dp)
	) {
		BluetoothSwitch(bluetooth) {
			bluetooth = it
			onBluetoothChange(it)
		}
		Spacer(Modifier.width(20.dp))

		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.End
		) {
			if (bluetooth) {
				TextAndButton(
					text = "Conecte a el robot :",
					isBluetooth = true
				) {
					if (connectionManager.proveBluetoothDevices(pairedDevices)) {
						menuDevicesState = !menuDevicesState
					}
				}
				BluetoothDropMenu(
					state = menuDevicesState,
					onStateChange = { menuDevicesState = it },
					content = {
						pairedDevices.forEach { device ->
							DeviceItem(device) {
								connectionManager.conectToBluetoth(device, context)
								connectionManager.listenForBluetoothMessages()
								menuDevicesState = false
							}
						}
					}
				)
			} else {
				WifiTextField(
					connectionManager = connectionManager,
					ip = ip
				) {
					ip = it
				}
			}
		}
	}
}

@Composable
private fun TopBarForMainPageEnd(
	modeSelected: (Modes) -> Unit,
	navController: NavController,
	buttonHeight: (Int) -> Unit,
	buttonWidth: (Int) -> Unit,
	padding: (Int) -> Unit
) {
	val defaultButtonSize = DefaultButtonSize()
	var buttonHeight by remember { mutableFloatStateOf(defaultButtonSize.height) }
	var buttonWidth by remember { mutableFloatStateOf(defaultButtonSize.width) }
	var paddings by remember { mutableFloatStateOf(defaultButtonSize.padding) }

	var menuModeState by remember { mutableStateOf(false) }
	var modeSelect by remember { mutableStateOf(Modes.MANUAL) }

	var menuDiagramasState by remember { mutableStateOf(false) }

	var menuSettingState by remember { mutableStateOf(false) }

	val allDirectionsAndModes = remember {
		listOf(
			Directions.UP,
			Directions.DOWN,
			Directions.LEFT,
			Directions.RIGHT,
			Directions.UP_LEFT,
			Directions.UP_RIGHT,
			Directions.DOWN_LEFT,
			Directions.DOWN_RIGHT,
			Directions.STOP,
			Modes.MANUAL,
			Modes.AUTOMATA
		)
	}

	Row(
		horizontalArrangement = Arrangement.End,
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.fillMaxWidth()
			.padding(end = 20.dp)
	) {
		Row(verticalAlignment = Alignment.CenterVertically) {
			TextAndButton("modo :") {
				menuModeState = !menuModeState
			}
			ModeDropMenu(
				state = menuModeState,
				onStateChange = { menuModeState = it },
				content = {
					ModeIcon(
						text = "Automata",
						onClick = {
							modeSelect = Modes.AUTOMATA
							menuModeState = false
							modeSelected(modeSelect)
						},
						stateOfItem = modeSelect == Modes.AUTOMATA
					)
					ModeIcon(
						text = "control manual",
						onClick = {
							modeSelect = Modes.MANUAL
							menuModeState = false
							modeSelected(modeSelect)
						},
						stateOfItem = modeSelect == Modes.MANUAL
					)
				}
			)
		}
		Spacer(Modifier.width(10.dp))

		Row(verticalAlignment = Alignment.CenterVertically) {
			TextAndButton("diagramas :") {
				menuDiagramasState = !menuDiagramasState
			}
			DiagramaDropMenu(
				state = menuDiagramasState,
				onStateChange = { menuDiagramasState = it },
				content = {
					DiagramaItem("ESP 32") {
						navController.navigate(AppScreen.ESP32Page.route)
					}
					/*
					DiagramaItem("ESP 8622") {
						navController.navigate(AppScreen.ESP8622Page.route)
					}
					*/
					DiagramaItem("Ardiuno y hc-05") {
						navController.navigate(AppScreen.ArduinoOneAndHC05Page.route)
					}
				}
			)
		}
		Spacer(modifier = Modifier.width(10.dp))

		Row(verticalAlignment = Alignment.CenterVertically) {
			IconsButtons(
				onClick = { menuSettingState = !menuSettingState },
				isSolidColor = false,
			)
			SettingsDropMenu(
				state = menuSettingState,
				onStateChange = { menuSettingState = it },
				content = {
					// items para cambiar los tamaños de los botones y padding
					SliderForConfiguration(
						value = buttonWidth,
						onValueChange = {
							buttonWidth = it
							buttonWidth(it.toInt())
						},
						typeForReset = ButtonSize.WIDTH,
						valueRange = 100f..300f,
						ruta = painterResource(id = R.drawable.group_1)
					)
					SliderForConfiguration(
						value = buttonHeight,
						onValueChange = {
							buttonHeight = it
							buttonHeight(it.toInt())
						},
						valueRange = 100f..300f,
						ruta = painterResource(id = R.drawable.group_3)
					)
					SliderForConfiguration(
						value = paddings,
						onValueChange = {
							paddings = it
							padding(it.toInt())
						},
						typeForReset = ButtonSize.PADDING,
						valueRange = 0f..50f,
						ruta = painterResource(id = R.drawable.group_4__1_)
					)
					// items para cambiar los caracteres de las direcciones y modos
					allDirectionsAndModes.forEach { it ->
						SettingsItem(it)
					}
				}
			)
		}
	}

}

@Composable
fun TopBarForMainPage(
	bluetoothAdapter: BluetoothAdapter,
	connectionManager: ConnectionViewModel,
	navController: NavController,
	modeSelected: (Modes) -> Unit,
	buttonWidthValue: (Int) -> Unit,
	buttonHeightValue: (Int) -> Unit,
	paddingValues: (Int) -> Unit
) {
	Box(
		contentAlignment = Alignment.Center,
		modifier = Modifier
			.fillMaxWidth()
			.background(Blue)
			.padding(vertical = 7.dp)
	) {
		TopBarForMainPageStart(
			onBluetoothChange = { connectionManager.isBluetooth = it },
			connectionManager = connectionManager,
			bluetoothAdapter = bluetoothAdapter
		)

		TopBarForMainPageEnd(
			modeSelected = { modeSelected(it) },
			navController = navController,
			buttonHeight = { buttonHeightValue(it) },
			buttonWidth = { buttonWidthValue(it) },
			padding = { paddingValues(it) }
		)
	}
}