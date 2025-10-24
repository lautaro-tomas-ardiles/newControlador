package com.example.newcontrolador.utilitis

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.newcontrolador.R
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
	listOfSliders: List<SliderConfig>,
	listOfThemes: List<ThemeConfig>
) {
	val scroll = rememberScrollState()

	DropdownMenu(
		expanded = state,
		onDismissRequest = { onStateChange(false) },
		modifier = Modifier
			.background(MaterialTheme.colorScheme.tertiary)
			.wrapContentSize()
	) {
		Column(
			Modifier.padding(
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
			TextAndButton(
				text = "configuracion completa ",
				isPainter = true,
				painter = painterResource(R.drawable.external_link),
				tintColor = MaterialTheme.colorScheme.background
			) {
				navController.navigate(AppScreen.SettingsPage.route)
			}
			listOfSliders.forEach { config ->
				SliderForConfiguration(
					value = config.value,
					onValueChange = config.onValueChange,
					valueRange = config.valueRange,
					ruta = config.ruta,
					typeForReset = config.typeForReset
				)
			}
		}
	}
}
