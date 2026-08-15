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
import androidx.compose.runtime.LaunchedEffect
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
import com.example.newcontrolador.data.configs.DirectionsConfig
import com.example.newcontrolador.data.enums.ModesEnum
import com.example.newcontrolador.data.enums.SliderType
import com.example.newcontrolador.data.enums.ThemeType
import com.example.newcontrolador.storage.DataStoreViewModel
import com.example.newcontrolador.navigation.AppScreen

/**
 * TopBar genérica con título y botón de navegación.
 *
 * @param text Texto que se mostrará en la barra superior.
 * @param navController Controlador de navegación para manejar la acción de retroceso.
 * @param buttonsUtils Clase con los botones personalizados para la barra superior.
 */
@Composable
fun SecondaryTopBar(
	text: String,
	navController: NavController,
	buttonsUtils: ButtonsUtils
) {
	CenterAlignedTopAppBar(
		title = { Text(text = text) },
		navigationIcon = {
			Row {
				buttonsUtils.ImageVector(
					onClick = { navController.navigate(AppScreen.MainPage.route) },
					image = Icons.AutoMirrored.Outlined.ArrowBack,
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
 * Clase de utilidades para la barra superior (TopBar) de la página principal.
 *
 * @property bluetoothAdapter Adaptador de Bluetooth para gestionar conexiones Bluetooth.
 * @property connectionManager ViewModel para manejar la lógica de conexión y estado de Bluetooth/WiFi.
 * @property dataStore ViewModel para acceder a la configuración almacenada, como temas y configuraciones de movimiento.
 * @property buttonsUtils Clase con botones personalizados para usar en la barra superior.
 */
class TopBarUtils(
	private val bluetoothAdapter: BluetoothAdapter,
	private val connectionManager: ConnectionViewModel,
	private val dataStore: DataStoreViewModel,
	private val buttonsUtils: ButtonsUtils
) {
	/**
	 * Sección izquierda de la TopBar en la página principal.
 	 *
	 * Muestra un switch para alternar Bluetooth/WiFi, un botón para conectarse al robot
	 * y un menú desplegable de dispositivos disponibles.
 	 *
	 * @param onBluetoothChange Función que se ejecuta al cambiar el estado del Bluetooth.
	 * @param directionsConfig Configuración de direcciones para la conexión Bluetooth.
	 * @param modifier Modificador para aplicar estilos a la fila contenedora de esta sección.
	 */
	@Composable
	private fun MainTopBarStart(
		onBluetoothChange: (Boolean) -> Unit,
		directionsConfig: DirectionsConfig,
		modifier: Modifier
	){
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
			modifier = modifier.fillMaxWidth()
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
					buttonsUtils.Text("Conecte a el robot :") {
						it.Bluetooth {
							if (connectionManager.verifyBluetoothDevices(pairedDevices)) {
								menuDevicesState = !menuDevicesState
							}
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
						ip = ip,
						buttonsUtils = buttonsUtils
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
	 * @param buttonsUtils class con los botones personalizados.
	 * @param modifier Modificador para aplicar estilos a la fila contenedora de esta sección.
	 */
	@Composable
	private fun MainTopBarEnd(
		modeSelected: (ModesEnum) -> Unit,
		navController: NavController,
		modifier: Modifier
	) {
		val configButton by dataStore.movementConfig.collectAsState()
		val selectedTheme by dataStore.theme.collectAsState()
		val configVelocity by dataStore.velocityChar.collectAsState()

		val value = when (configVelocity.velocityChar) {
			in '0'..'9' -> (configVelocity.velocityChar - '0') * 10f
			'q' -> 100f
			else -> 0f
		}

		var buttonHeight by remember { mutableFloatStateOf(configButton.height) }
		var buttonWidth by remember { mutableFloatStateOf(configButton.width) }
		var paddings by remember { mutableFloatStateOf(configButton.padding) }

		var velocity by remember { mutableFloatStateOf(value) }

		var menuModeState by remember { mutableStateOf(false) }
		var modeSelect by remember { mutableStateOf(ModesEnum.MANUAL) }

		var menuDiagramasState by remember { mutableStateOf(false) }
		var menuSettingState by remember { mutableStateOf(false) }

		val modes = setOf(
			ModesEnum.AUTOMATA,
			ModesEnum.MANUAL
		)
		val slidersList = listOf(
			SliderConfig(
				value = velocity,
				onValueChange = {
					velocity = it
					val char = when {
						velocity < 100f -> ('0' + (it / 10).toInt())
						velocity == 100f -> 'q'
						else -> '0'
					}
					dataStore.setVelocityChar(char)
				},
				valueRange = 0f..100f,
				sliderType = SliderType.VELOCITY,
				ruta = painterResource(R.drawable.velocity)
			),
			SliderConfig(
				value = buttonHeight,
				onValueChange = {
					buttonHeight = (it * 100).toInt() / 100f
					dataStore.setButtonHeight(buttonHeight)
				},
				valueRange = 0f..1f,
				sliderType = SliderType.HEIGHT,
				ruta = painterResource(id = R.drawable.height)
			),
			SliderConfig(
				value = buttonWidth,
				onValueChange = {
					buttonWidth = (it * 100).toInt() / 100f
					dataStore.setButtonWidth(buttonWidth)
				},
				valueRange = 0f..1f,
				sliderType = SliderType.WIDTH,
				ruta = painterResource(id = R.drawable.width)
			),
			SliderConfig(
				value = paddings,
				onValueChange = {
					paddings = it
					dataStore.setButtonPadding(paddings)
				},
				valueRange = 0f..50f,
				sliderType = SliderType.PADDING,
				ruta = painterResource(id = R.drawable.padding)
			)
		)
		val themesList = listOf(
			ThemeConfig(
				isColorSelected = (selectedTheme == ThemeType.DEFAULT),
				onClick = { dataStore.setTheme(ThemeType.DEFAULT) },
				theme = ThemeType.DEFAULT
			),
			ThemeConfig(
				isColorSelected = (selectedTheme == ThemeType.WHITE),
				onClick = { dataStore.setTheme(ThemeType.WHITE) },
				theme = ThemeType.WHITE
			)
		)

		LaunchedEffect(Unit, configVelocity.velocityChar) {
			connectionManager.sendChar(configVelocity.velocityChar)
		}
		Row(
			horizontalArrangement = Arrangement.End,
			verticalAlignment = Alignment.CenterVertically,
			modifier = modifier.fillMaxWidth()
		) {
			Row(verticalAlignment = Alignment.CenterVertically) {
				buttonsUtils.Text("acerca de:") {
					it.Painter(
						onClick = { navController.navigate(AppScreen.AboutPage.route) },
						imageRes = R.drawable.alerta,
						border = true
					)
				}
			}
			Spacer(Modifier.width(10.dp))

			Row(verticalAlignment = Alignment.CenterVertically) {
				buttonsUtils.Text("modo :") {
					it.Painter(
						onClick = { menuModeState = !menuModeState },
						imageRes = R.drawable.vert_more,
						border = true
					)
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
				buttonsUtils.Text("digramas :") {
					it.Painter(
						onClick = { menuDiagramasState = !menuDiagramasState },
						imageRes = R.drawable.vert_more,
						border = true
					)
				}
				DiagramaDropMenu(
					state = menuDiagramasState,
					onStateChange = { menuDiagramasState = it },
					content = {
						DiagramaItem("ESP 32") {
							navController.navigate(AppScreen.ESP32Page.route)
						}
						DiagramaItem("Ardiuno y hc-05") {
							navController.navigate(AppScreen.ArduinoOneAndHC05Page.route)
						}
					}
				)
			}
			Spacer(Modifier.width(10.dp))

			Row(verticalAlignment = Alignment.CenterVertically) {
				buttonsUtils.Painter(
					onClick = { menuSettingState = !menuSettingState },
					imageRes = R.drawable.settings
				)
				SettingsDropMenu(
					state = menuSettingState,
					onStateChange = { menuSettingState = it },
					listOfSliders = slidersList,
					listOfThemes = themesList,
					navController = navController,
					buttonsUtils = buttonsUtils
				)
			}
			Spacer(Modifier.width(10.dp))
		}
	}

	/**
	 * Barra superior principal que integra las secciones izquierda y derecha.
	 *
	 * @param navController Controlador de navegación para manejar las acciones de los botones y menús.
	 * @param modeSelected Función que se ejecuta al seleccionar un modo en el menú de modos.
	 * @param directionsConfig Configuración de direcciones para la conexión Bluetooth, necesaria para la sección izquierda.
	 * @receiver
	 */
	@Composable
	fun MainTopBar(
		navController: NavController,
		modeSelected: (ModesEnum) -> Unit,
		directionsConfig: DirectionsConfig
	) {
		val horizontalPadding = 20.dp

		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier
				.fillMaxWidth()
				.background(MaterialTheme.colorScheme.primary)
				.padding(vertical = 8.dp)
		) {
			MainTopBarStart(
				onBluetoothChange = { connectionManager.isBluetooth = it },
				directionsConfig = directionsConfig,
				modifier = Modifier.padding(start = horizontalPadding)
			)

			MainTopBarEnd(
				modeSelected = { modeSelected(it) },
				navController = navController,
				modifier = Modifier.padding(end = horizontalPadding)
			)
		}
	}
}