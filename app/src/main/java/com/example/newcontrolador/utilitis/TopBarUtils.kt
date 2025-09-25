@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.newcontrolador.utilitis

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.example.newcontrolador.connection.Directions
import com.example.newcontrolador.connection.Modes
import com.example.newcontrolador.connection.WiFiConnectionManager
import com.example.newcontrolador.navigation.AppScreen
import com.example.newcontrolador.ui.theme.Black
import com.example.newcontrolador.ui.theme.Blue
import com.example.newcontrolador.ui.theme.LightGreen
import com.example.newcontrolador.ui.theme.NewControladorTheme

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
fun TopBarForMainPage(
	bluetoothAdapter: BluetoothAdapter,
	wiFiConnectionManager: WiFiConnectionManager,
	navController: NavController,
	isBluetoothEnable: (Boolean) -> Unit,
	devicesChange: (Boolean) -> Unit,
	modeSelected: (Modes) -> Unit
) {
	var bluetooth by remember { mutableStateOf(true) }
	var ip by remember { mutableStateOf("") }

	var menuModeState by remember { mutableStateOf(false) }
	var modeSelect by remember { mutableStateOf(Modes.MANUAL) }

	var menuDiagramasState by remember { mutableStateOf(false) }

	var menuSettingState by remember { mutableStateOf(false) }

	val context = LocalContext.current

	val pairedDevices: Set<BluetoothDevice>
	if (
		ActivityCompat.checkSelfPermission(
			context,
			Manifest.permission.BLUETOOTH_CONNECT
		) == PackageManager.PERMISSION_GRANTED
	) {
		pairedDevices = bluetoothAdapter.bondedDevices
	} else {
		pairedDevices = setOf()
	}

	val allDirections = remember {
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

	Box(
		contentAlignment = Alignment.Center,
		modifier = Modifier
			.fillMaxWidth()
			.background(Blue)
			.padding(vertical = 7.dp)
	) {
		Row(
			horizontalArrangement = Arrangement.Start,
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.fillMaxWidth()
				.padding(start = 20.dp)
		) {
			BluetoothSwitch(bluetooth) {
				bluetooth = it
				isBluetoothEnable(it)
			}
			Spacer(Modifier.width(20.dp))

			if (bluetooth) {
				TextAndButton(
					text = "Conecte a el robot :",
					isBluetooth = true
				) {
					if (pairedDevices.isEmpty()) {
						Toast.makeText(
							context,
							"No se encontraron dispositivos",
							Toast.LENGTH_SHORT
						).show()
					} else {
						devicesChange(true)
					}
				}
			} else {
				WifiTextField(
					wifiManager = wiFiConnectionManager,
					ip = ip
				) { newIp ->
					ip = newIp
				}
			}
		}
		Row(
			horizontalArrangement = Arrangement.End,
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.fillMaxWidth()
				.padding(end = 20.dp)
		) {
			Row (verticalAlignment = Alignment.CenterVertically) {
				TextAndButton("modo :") {
					menuModeState = !menuModeState
				}
				DropdownMenu(
					expanded = menuModeState,
					onDismissRequest = { menuModeState = false },
					modifier = Modifier
						.background(LightGreen)
						.wrapContentSize()
				) {
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
			}
			Spacer(Modifier.width(10.dp))

			Row (verticalAlignment = Alignment.CenterVertically) {
				TextAndButton("diagramas :") {
					menuDiagramasState = !menuDiagramasState
				}
				DropdownMenu(
					expanded = menuDiagramasState,
					onDismissRequest = { menuDiagramasState = false },
					modifier = Modifier
						.background(LightGreen)
						.wrapContentSize()
				) {
					DropdownMenuItem(
						text = {
							Text(text = "ESP 32", color = Black)
						},
						onClick = { navController.navigate(AppScreen.ESP32Page.route) }
					)
					DropdownMenuItem(
						text = {
							Text(text = "ESP 8622", color = Black)
						},
						onClick = { navController.navigate(AppScreen.ESP8622Page.route) }
					)
					DropdownMenuItem(
						text = {
							Text(text = "Ardiuno y hc-05", color = Black)
						},
						onClick = { navController.navigate(AppScreen.ArduinoOneAndHC05Page.route) }
					)
				}
			}
			Spacer(modifier = Modifier.width(10.dp))

			Row (verticalAlignment = Alignment.CenterVertically) {
				IconsButtons(
					onClick = { menuSettingState = !menuSettingState },
					isSolidColor = false,
				)
				DropdownMenu(
					expanded = menuSettingState,
					onDismissRequest = { menuSettingState = false },
					modifier = Modifier
						.background(LightGreen)
						.wrapContentSize()
						.heightIn(max = 300.dp)
				) {
					allDirections.forEach { it ->
						SettingsItem(it)
					}
				}
			}
		}
	}
}

/**
 * preview de las otras funciones
 * */
@Composable
fun TopBar2ForPreview(text: String) {
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
					onClick = { /* No action in preview */ },
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
fun TopBarForPreview(
	wiFiConnectionManager: WiFiConnectionManager,
	isBluetoothEnable: (Boolean) -> Unit,
	modeSelected: (Modes) -> Unit
) {
	var bluetooth by remember { mutableStateOf(true) }
	var ip by remember { mutableStateOf("") }

	var menuModeState by remember { mutableStateOf(false) }
	var modeSelect by remember { mutableStateOf(Modes.MANUAL) }

	var menuDiagramasState by remember { mutableStateOf(false) }

	var menuSettingState by remember { mutableStateOf(false) }

	val allDirections = remember {
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

	Box(
		contentAlignment = Alignment.Center,
		modifier = Modifier
			.fillMaxWidth()
			.background(Blue)
			.padding(vertical = 7.dp)
	) {
		Row(
			horizontalArrangement = Arrangement.Start,
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.fillMaxWidth()
				.padding(start = 20.dp)
		) {
			BluetoothSwitch(bluetooth) {
				bluetooth = it
				isBluetoothEnable(it)
			}
			Spacer(Modifier.width(20.dp))

			if (bluetooth) {
				TextAndButton(
					text = "Conecte a el robot :",
					isBluetooth = true
				) { }
			} else {
				WifiTextField(
					wifiManager = wiFiConnectionManager,
					ip = ip
				) { newIp ->
					ip = newIp
				}
			}
		}
		Row(
			horizontalArrangement = Arrangement.End,
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.fillMaxWidth()
				.padding(end = 20.dp)
		) {
			Row (verticalAlignment = Alignment.CenterVertically) {
				TextAndButton("modo :") {
					menuModeState = !menuModeState
				}
				DropdownMenu(
					expanded = menuModeState,
					onDismissRequest = { menuModeState = false },
					modifier = Modifier
						.background(LightGreen)
						.wrapContentSize()
				) {
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
			}
			Spacer(Modifier.width(10.dp))

			Row (verticalAlignment = Alignment.CenterVertically) {
				TextAndButton("diagramas :") {
					menuDiagramasState = !menuDiagramasState
				}
				DropdownMenu(
					expanded = menuDiagramasState,
					onDismissRequest = { menuDiagramasState = false },
					modifier = Modifier
						.background(LightGreen)
						.wrapContentSize()
				) {
					DropdownMenuItem(
						text = {
							Text(text = "ESP 32", color = Black)
						},
						onClick = {  }
					)
					DropdownMenuItem(
						text = {
							Text(text = "ESP 8622", color = Black)
						},
						onClick = {  }
					)
					DropdownMenuItem(
						text = {
							Text(text = "Ardiuno y hc-05", color = Black)
						},
						onClick = {  }
					)
				}
			}
			Spacer(modifier = Modifier.width(10.dp))

			Row (verticalAlignment = Alignment.CenterVertically) {
				IconsButtons(
					onClick = { menuSettingState = !menuSettingState },
					isSolidColor = false,
				)
				DropdownMenu(
					expanded = menuSettingState,
					onDismissRequest = { menuSettingState = false },
					modifier = Modifier
						.background(LightGreen)
						.wrapContentSize()
						.heightIn(max = 300.dp)
				) {
					allDirections.forEach { it ->
						SettingsItem(it)
					}
				}
			}
		}
	}
}

@Preview(
	device = "spec:parent=pixel_5,orientation=landscape",
	showBackground = true
)
@Composable
private fun PreviewA() {
	val wifiManager = remember { WiFiConnectionManager() }

	NewControladorTheme {
		Column {
			TopBarForPreview(
				wiFiConnectionManager = wifiManager,
				isBluetoothEnable = { },
				modeSelected = { }
			)
			Spacer(Modifier.height(20.dp))
			TopBar2ForPreview("diagrama para esp 32")
		}
	}
}