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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newcontrolador.connection.data.DirectionsEnum
import com.example.newcontrolador.connection.data.ModesEnum
import com.example.newcontrolador.data.DataStoreViewModel
import com.example.newcontrolador.utilitis.ButtonsUtils
import com.example.newcontrolador.utilitis.LineAndText
import com.example.newcontrolador.utilitis.SecondaryTopBar
import com.example.newcontrolador.utilitis.SetOrientation
import com.example.newcontrolador.utilitis.SettingsItemForDirections
import com.example.newcontrolador.utilitis.SettingsItemForModes

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
