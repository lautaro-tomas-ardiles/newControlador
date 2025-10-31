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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newcontrolador.connection.data.Directions
import com.example.newcontrolador.connection.data.Modes
import com.example.newcontrolador.data.DataStoreViewModel
import com.example.newcontrolador.utilitis.LineAndText
import com.example.newcontrolador.utilitis.SetOrientation
import com.example.newcontrolador.utilitis.SettingsItemForDirections
import com.example.newcontrolador.utilitis.SettingsItemForModes
import com.example.newcontrolador.utilitis.SimpleButton
import com.example.newcontrolador.utilitis.TopBar2

@Composable
fun MainSettingsPage(
	navController: NavController,
	viewModel: DataStoreViewModel
) {
	SetOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, LocalContext.current)

	var reloadKey by remember { mutableIntStateOf(0) }
	val scroll = rememberScrollState()

	// Si cambia reloadKey, todo el contenido se recompondrá
	key(reloadKey) {
		MainSettingsPageContent(navController, viewModel, scroll) {
			reloadKey++ // acción que fuerza la recarga
		}
	}
}

@Composable
fun MainSettingsPageContent(
	navController: NavController,
	viewModel: DataStoreViewModel,
	scroll: ScrollState,
	onReload: () -> Unit
) {
	val modes = listOf(
		Modes.AUTOMATA,
		Modes.MANUAL
	)
	val directions = listOf(
		Directions.UP,
		Directions.DOWN,
		Directions.LEFT,
		Directions.DOWN_LEFT,
		Directions.UP_LEFT,
		Directions.RIGHT,
		Directions.DOWN_RIGHT,
		Directions.UP_RIGHT,
		Directions.STOP,
	)

	Scaffold(
		topBar = {
			TopBar2(
				"Configuración completa",
				navController
			) },
		containerColor = MaterialTheme.colorScheme.background
	) { padding ->
		Column(
			Modifier
				.padding(padding)
				.padding(horizontal = 10.dp, vertical = 30.dp)
				.verticalScroll(scroll),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			LineAndText("Ajustes de modos")

			SimpleButton("resetear modos") {
				viewModel.resetModesToDefault()
				onReload() // recargar la pantalla
			}

			modes.forEach { mode ->
				SettingsItemForModes(mode, viewModel)
			}

			Spacer(Modifier.padding(5.dp))

			LineAndText("Ajustes de direcciones")

			SimpleButton("resetear direcciones") {
				viewModel.resetDirectionChars()
				onReload() // recargar la pantalla
			}

			directions.forEach { direction ->
				SettingsItemForDirections(direction, viewModel)
			}
		}
	}
}
