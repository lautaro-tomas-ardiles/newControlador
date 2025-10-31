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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.newcontrolador.connection.data.Buttons
import com.example.newcontrolador.connection.data.DirectionsConfig
import com.example.newcontrolador.connection.data.Modes
import com.example.newcontrolador.connection.data.ThemeType
import com.example.newcontrolador.data.DataStoreViewModel
import com.example.newcontrolador.navigation.AppScreen

/**
 * TopBar genérica con título y botón de navegación.
 *
 * @param text Texto que se mostrará en la barra superior.
 * @param navController Controlador de navegación para manejar la acción de retroceso.
 */
@Composable
fun TopBar2(text: String, navController: NavController) {
	CenterAlignedTopAppBar(
		title = { Text(text = text) },
		navigationIcon = {
			Row {
				IconsButtonsCustom(
					onClick = { navController.navigate(AppScreen.MainPage.route) },
					imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
					tintColor = MaterialTheme.colorScheme.background
				)
			}
		},
		colors = TopAppBarDefaults.topAppBarColors(
			containerColor = MaterialTheme.colorScheme.secondary,
			titleContentColor = MaterialTheme.colorScheme.background,
			navigationIconContentColor = MaterialTheme.colorScheme.background
		)
	)
}

/**
 * Sección izquierda de la TopBar en la página principal.
 *
 * Muestra un switch para alternar Bluetooth/WiFi, un botón para conectarse al robot
 * y un menú desplegable de dispositivos disponibles.
 *
 * @param onBluetoothChange Función que se ejecuta al cambiar el estado del Bluetooth.
 * @param connectionManager Manager para manejar conexiones Bluetooth/WiFi.
 * @param bluetoothAdapter Adaptador Bluetooth del dispositivo.
 */
@Composable
private fun TopBarForMainPageStart(
	onBluetoothChange: (Boolean) -> Unit,
	connectionManager: ConnectionViewModel,
	bluetoothAdapter: BluetoothAdapter,
	directionsConfig: DirectionsConfig
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
					if (connectionManager.verifyBluetoothDevices(pairedDevices)) {
						menuDevicesState = !menuDevicesState
					}
				}
				BluetoothDropMenu(
					state = menuDevicesState,
					onStateChange = { menuDevicesState = it },
					setOfDevices = pairedDevices,
					connectionManager = connectionManager,
					context = context,
					directionsConfig = directionsConfig
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

/**
 * Sección derecha de la TopBar en la página principal.
 *
 * Muestra los menús de modos, diagramas y configuración de sliders para botones.
 *
 * @param modeSelected Función que se ejecuta al seleccionar un modo.
 * @param navController Controlador de navegación para los diagramas.
 * @param viewModel ViewModel para manejar la configuración almacenada.
 */
@Composable
private fun TopBarForMainPageEnd(
	modeSelected: (Modes) -> Unit,
	navController: NavController,
	viewModel: DataStoreViewModel
) {
	val configButton by viewModel.buttonConfig.collectAsState()

	val selectedTheme by viewModel.theme.collectAsState()

	var buttonHeight by remember { mutableFloatStateOf(configButton.height) }
	var buttonWidth by remember { mutableFloatStateOf(configButton.width) }
	var paddings by remember { mutableFloatStateOf(configButton.padding) }

	var menuModeState by remember { mutableStateOf(false) }
	var modeSelect by remember { mutableStateOf(Modes.MANUAL) }

	var menuDiagramasState by remember { mutableStateOf(false) }

	var menuSettingState by remember { mutableStateOf(false) }

	val modes = setOf(
		Modes.AUTOMATA,
		Modes.MANUAL
	)
	val slidersList = listOf(
		SliderConfig(
			value = buttonHeight,
			onValueChange = {
				buttonHeight = it
				viewModel.setButtonHeight(it)
			},
			valueRange = 100f..300f,
			ruta = painterResource(id = R.drawable.height)
		),
		SliderConfig(
			value = buttonWidth,
			onValueChange = {
				buttonWidth = it
				viewModel.setButtonWidth(it)
			},
			valueRange = 100f..300f,
			typeForReset = Buttons.WIDTH,
			ruta = painterResource(id = R.drawable.width)
		),
		SliderConfig(
			value = paddings,
			onValueChange = {
				paddings = it
				viewModel.setButtonPadding(it)
			},
			valueRange = 0f..50f,
			typeForReset = Buttons.PADDING,
			ruta = painterResource(id = R.drawable.padding)
		)
	)
	val themesList = listOf(
		ThemeConfig(
			isColorSelected = selectedTheme == ThemeType.DEFAULT,
			onClick = {
				viewModel.setTheme(ThemeType.DEFAULT)
			},
			theme = ThemeType.DEFAULT
		),
		ThemeConfig(
			isColorSelected = selectedTheme == ThemeType.WHITE,
			onClick = {
				viewModel.setTheme(ThemeType.WHITE)
			},
			theme = ThemeType.WHITE
		)
	)

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
				setOfModes = modes,
				onClick = { mode ->
					modeSelect = mode
					menuModeState = false
					modeSelected(mode)
				},
				modeSelect = modeSelect
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
			IconsButtonsCustom(
				onClick = { menuSettingState = !menuSettingState },
				isSolidColor = false,
			)
			SettingsDropMenu(
				state = menuSettingState,
				onStateChange = { menuSettingState = it },
				listOfSliders = slidersList,
				listOfThemes = themesList,
				navController = navController
			)
		}
	}

}

/**
 * TopBar completa para la página principal.
 *
 * Combina la sección izquierda (Bluetooth/WiFi) y derecha (modos, diagramas, sliders) en un
 * solo componente con fondo azul.
 *
 * @param bluetoothAdapter Adaptador Bluetooth del dispositivo.
 * @param connectionManager Manager de conexión Bluetooth/WiFi.
 * @param navController Controlador de navegación.
 * @param modeSelected Función que se ejecuta al seleccionar un modo.
 * @param viewModel ViewModel para manejar la configuración almacenada.
 * @param directionsConfig Direcciones de configuración para la conexión.
 */
@Composable
fun TopBarForMainPage(
	bluetoothAdapter: BluetoothAdapter,
	connectionManager: ConnectionViewModel,
	navController: NavController,
	viewModel: DataStoreViewModel,
	modeSelected: (Modes) -> Unit,
	directionsConfig: DirectionsConfig
) {
	Box(
		contentAlignment = Alignment.Center,
		modifier = Modifier
			.fillMaxWidth()
			.background(MaterialTheme.colorScheme.primary)
			.padding(vertical = 7.dp)
	) {
		TopBarForMainPageStart(
			onBluetoothChange = { connectionManager.isBluetooth = it },
			connectionManager = connectionManager,
			bluetoothAdapter = bluetoothAdapter,
			directionsConfig = directionsConfig
		)

		TopBarForMainPageEnd(
			modeSelected = { modeSelected(it) },
			navController = navController,
			viewModel = viewModel
		)
	}
}