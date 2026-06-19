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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newcontrolador.R
import com.example.newcontrolador.connection.data.ButtonsEnum
import com.example.newcontrolador.connection.data.DirectionsEnum
import com.example.newcontrolador.data.store.DataStoreViewModel
import com.example.newcontrolador.utilitis.Buttons
import com.example.newcontrolador.utilitis.LineAndText
import com.example.newcontrolador.utilitis.SetOrientation
import com.example.newcontrolador.utilitis.SettingsItemForDirections
import com.example.newcontrolador.utilitis.SettingsTopBar
import com.example.newcontrolador.utilitis.SliderForConfiguration

@Composable
fun MainSettingsPage(navController: NavController, viewModel: DataStoreViewModel) {
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
	val button = Buttons()
	val slider = SliderForConfiguration()

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

	val configButton by viewModel.buttonConfig.collectAsState()
	val configVelocity by viewModel.velocityChar.collectAsState()

	val value = when (val c = configVelocity.velocityChar) {
		in '0'..'9' -> (c - '0') * 10f
		'q' -> 100f
		else -> 0f
	}

	var buttonHeight by remember { mutableFloatStateOf(configButton.height) }
	var buttonWidth by remember { mutableFloatStateOf(configButton.width) }

	var velocity by remember { mutableFloatStateOf(value) }

	Scaffold(
		topBar = {
			SettingsTopBar(
				title = "Ajustes",
				navController = navController
			)
		},
		containerColor = MaterialTheme.colorScheme.background
	) { padding ->
		Column(
			modifier = Modifier
				.padding(padding)
				.padding(horizontal = 10.dp, vertical = 30.dp)
				.verticalScroll(scroll),
			horizontalAlignment = Alignment.Start
		) {
			LineAndText("Ajustes de velocidad")

			slider.Slider(
				value = velocity,
				onValueChange = { newValue ->
					velocity = newValue
					val char = when {
						newValue < 100f -> ('0' + (newValue / 10).toInt())
						newValue == 100f -> 'q'
						else -> '0'
					}
					viewModel.setVelocityChar(char)
				},
				valueRange = 0f..100f,
				ruta = painterResource(R.drawable.velocity),
				typeForReset = null
			)
			Spacer(Modifier.padding(5.dp))

			LineAndText("Ajustes del tamaño de los botones")

			slider.Slider(
				value = buttonHeight,
				onValueChange = { newValue ->
					buttonHeight = (newValue * 100).toInt() / 100f
					viewModel.setButtonHeight(buttonHeight)
				},
				valueRange = 0f..1f,
				ruta = painterResource(R.drawable.height)
			)
			slider.Slider(
				value = buttonWidth,
				onValueChange = { newValue ->
					buttonWidth = (newValue * 100).toInt() / 100f
					viewModel.setButtonWidth(buttonWidth)
				},
				valueRange = 0f..1f,
				ruta = painterResource(R.drawable.width),
				typeForReset = ButtonsEnum.WIDTH
			)
			Spacer(Modifier.padding(5.dp))

			LineAndText("Ajustes de direcciones")

			button.Simple("resetear direcciones") {
				viewModel.resetDirectionChars()
				onReload() // recargar la pantalla
			}
			directions.forEach { direction ->
				SettingsItemForDirections(direction, viewModel)
			}
		}
	}
}