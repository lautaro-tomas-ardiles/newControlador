package com.example.newcontrolador.screen

import android.content.pm.ActivityInfo
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newcontrolador.data.enums.DirectionsEnum
import com.example.newcontrolador.R
import com.example.newcontrolador.data.enums.ModesEnum
import com.example.newcontrolador.data.enums.SliderType
import com.example.newcontrolador.storage.DataStoreViewModel
import com.example.newcontrolador.utilitis.ButtonsUtils
import com.example.newcontrolador.utilitis.LineAndText
import com.example.newcontrolador.utilitis.SecondaryTopBar
import com.example.newcontrolador.utilitis.SetOrientation
import com.example.newcontrolador.utilitis.SettingsItemForDirections
import com.example.newcontrolador.utilitis.SettingsItemForModes
import com.example.newcontrolador.utilitis.Slider

@Composable
fun MainSettingsPage(
	navController: NavController,
	viewModel: DataStoreViewModel
) {
	val buttons by viewModel.buttonsConfig.collectAsState()
	val buttonsUtils = ButtonsUtils(
		sizeButton = buttons.button,
		sizeIcon = buttons.icon
	)

	SetOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, LocalContext.current)

	val directions by viewModel.directionChars.collectAsState()
	val modes by viewModel.modeChars.collectAsState()

	val scroll = rememberScrollState()

	// Si cambia reloadKey, todo el contenido se recompondrá
	key(directions, modes) {
		MainSettingsPageContent(navController, viewModel, buttonsUtils, scroll)
	}
}

@Composable
fun MainSettingsPageContent(
	navController: NavController,
	viewModel: DataStoreViewModel,
	buttonsUtils: ButtonsUtils,
	scroll: ScrollState
) {
	val buttonConfig by viewModel.buttonsConfig.collectAsState()
	var buttonSize by remember { mutableIntStateOf(buttonConfig.button) }
	var buttonIcon by remember { mutableIntStateOf(buttonConfig.icon) }

	val modes = listOf(
		ModesEnum.AUTOMATA,
		ModesEnum.MANUAL
	)
	val directions = listOf(
		DirectionsEnum.UP,
		DirectionsEnum.DOWN,
		DirectionsEnum.LEFT,
		DirectionsEnum.DOWN_LEFT,
		DirectionsEnum.UP_LEFT,
		DirectionsEnum.RIGHT,
		DirectionsEnum.DOWN_RIGHT,
		DirectionsEnum.UP_RIGHT,
		DirectionsEnum.STOP,
	)

	Scaffold(
		topBar = {
			SecondaryTopBar(
				text = "Configuración completa",
				navController = navController,
				buttonsUtils = buttonsUtils
			)
		},
		containerColor = MaterialTheme.colorScheme.background
	) { padding ->
		Column(
			Modifier
				.padding(padding)
				.padding(
					horizontal = 10.dp,
					vertical = 30.dp
				)
				.verticalScroll(scroll),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			LineAndText("Configuración de botones")

			Column() {
				Text(
					text = "Tamaño de los botones de utilidades",
					color = MaterialTheme.colorScheme.secondary
				)
				Slider(
					value = buttonSize.toFloat(),
					onValueChange = {
						buttonSize = it.toInt()
						viewModel.setButtonSize(buttonSize)
					},
					valueRange = 0f..60f,
					sliderType = SliderType.BUTTON,
					ruta = painterResource(id = R.drawable.tama_o),
					buttonsUtils = buttonsUtils,
					iconTint = MaterialTheme.colorScheme.secondary
				)
				Spacer(Modifier.padding(5.dp))

				Text(
					text = "Tamaño del icon de los botones",
					color = MaterialTheme.colorScheme.secondary
				)
				Slider(
					value = buttonIcon.toFloat(),
					onValueChange = {
						buttonIcon = it.toInt()
						viewModel.setIconSize(buttonIcon)
					},
					valueRange = 0f..60f,
					sliderType = SliderType.ICON,
					ruta = painterResource(id = R.drawable.tama_o),
					buttonsUtils = buttonsUtils,
					iconTint = MaterialTheme.colorScheme.secondary
				)
				Spacer(Modifier.padding(5.dp))
			}

			buttonsUtils.Painter(
				onClick = { /*TODO: no accion nesesaria*/ },
				border = true,
				imageRes = R.drawable.height
			)

			LineAndText("Ajustes de modos")

			buttonsUtils.Simple("resetear modos") {
				viewModel.resetModesToDefault()
			}
			modes.forEach { mode ->
				SettingsItemForModes(mode, viewModel)
			}

			Spacer(Modifier.padding(5.dp))

			LineAndText("Ajustes de direcciones")

			buttonsUtils.Simple("resetear direcciones") {
				viewModel.resetDirectionChars()
			}
			directions.forEach { direction ->
				SettingsItemForDirections(direction, viewModel)
			}
		}
	}
}