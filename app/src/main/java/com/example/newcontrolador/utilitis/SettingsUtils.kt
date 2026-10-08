package com.example.newcontrolador.utilitis

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.newcontrolador.R
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newcontrolador.data.configs.SliderConfig
import com.example.newcontrolador.data.configs.ThemeConfig
import com.example.newcontrolador.data.enums.ModesEnum
import com.example.newcontrolador.navigation.AppScreen

/**
 * Menú desplegable de configuración.
 *
 * Muestra sliders configurables dentro de un `DropdownMenu`.
 *
 * @param state Estado del menú: `true` si está abierto, `false` si está cerrado.
 * @param onStateChange Función que se ejecuta al cambiar el estado del menú.
 * @param listOfSliders Lista de configuraciones de sliders a mostrar.
 * @param listOfThemes Lista de configuraciones de temas a mostrar.
 * @param navController Controlador de navegación para moverse a la página de configuración completa.
 */
@Composable
fun SettingsDropMenu(
	state: Boolean,
	navController: NavController,
	onStateChange: (Boolean) -> Unit,
	modeSelected: (ModesEnum) -> Unit,
	listOfSliders: List<SliderConfig>,
	listOfThemes: List<ThemeConfig>,
	buttonsUtils: ButtonsUtils
) {
	val scroll = rememberScrollState()

	var menuModeState by remember { mutableStateOf(false) }
	var modeSelect by remember { mutableStateOf(ModesEnum.MANUAL) }

	var menuDiagramasState by remember { mutableStateOf(false) }

	val modes = setOf(
		ModesEnum.AUTOMATA,
		ModesEnum.MANUAL
	)
	DropdownMenu(
		expanded = state,
		onDismissRequest = { onStateChange(false) },
		modifier = Modifier
			.background(MaterialTheme.colorScheme.tertiary)
			.wrapContentHeight()
			.width(300.dp)
	) {
		Column(
			modifier = Modifier.padding(
				horizontal = 10.dp,
				vertical = 5.dp
			)
		) {
			Row(
				modifier = Modifier
					.horizontalScroll(scroll)
					.padding(vertical = 5.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				listOfThemes.forEach { themeConfig ->
					ThemeItem(
						isColorSelected = themeConfig.isColorSelected,
						onClick = { themeConfig.onClick() },
						theme = themeConfig.theme
					)
					Spacer(Modifier.width(5.dp))
				}
			}
			Spacer(Modifier.width(10.dp))

			Column(
				modifier = Modifier.fillMaxWidth(),
				verticalArrangement = Arrangement.Center
			) {
				buttonsUtils.Text("acerca de:") {
					it.Painter(
						onClick = { navController.navigate(AppScreen.AboutPage.route) },
						imageRes = R.drawable.alerta,
						border = true,
						tintColor = MaterialTheme.colorScheme.primary
					)
				}
				Spacer(Modifier.height(15.dp))

				buttonsUtils.Text("modo :") {
					it.Painter(
						onClick = { menuModeState = !menuModeState },
						imageRes = R.drawable.vert_more,
						border = true,
						tintColor = MaterialTheme.colorScheme.primary
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
				Spacer(Modifier.height(15.dp))

				buttonsUtils.Text("digramas :") {
					it.Painter(
						onClick = { menuDiagramasState = !menuDiagramasState },
						imageRes = R.drawable.vert_more,
						border = true,
						tintColor = MaterialTheme.colorScheme.primary
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

			buttonsUtils.Text("configuracion completa :") {
				it.Painter(
					onClick = { navController.navigate(AppScreen.SettingsPage.route) },
					imageRes = R.drawable.nueva_pantalla,
					tintColor = MaterialTheme.colorScheme.background
				)
			}
			listOfSliders.forEach { config ->
				Slider(
					value = config.value,
					onValueChange = config.onValueChange,
					valueRange = config.valueRange,
					ruta = config.ruta,
					sliderType = config.sliderType,
					buttonsUtils = buttonsUtils
				)
			}
		}
	}
}
