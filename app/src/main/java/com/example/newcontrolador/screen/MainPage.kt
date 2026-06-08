package com.example.newcontrolador.screen

import android.bluetooth.BluetoothAdapter
import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.newcontrolador.connection.*
import com.example.newcontrolador.connection.data.Modes
import com.example.newcontrolador.data.DataStoreViewModel
import com.example.newcontrolador.utilitis.CustomSnackbar
import com.example.newcontrolador.utilitis.MovementUtils
import com.example.newcontrolador.utilitis.SetOrientation
import com.example.newcontrolador.utilitis.TopBarForMainPage

@Composable
fun MainScreen(
    bluetoothAdapter: BluetoothAdapter,
    navController: NavController,
    viewModel: DataStoreViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }

	val directions by viewModel.directionChars.collectAsState()
	val modes by viewModel.modeChars.collectAsState()
	val velocity by viewModel.velocityChar.collectAsState()

    var modeSelected by remember { mutableStateOf(Modes.MANUAL) }

    val bluetoothConnectionManager = remember { BluetoothConnectionManager() }
    val wifiManager = remember { WiFiConnectionManager() }
    val connectionManager = remember {
        ConnectionViewModel(
            bluetoothConnectionManager = bluetoothConnectionManager,
            wifiConnectionManager = wifiManager
        )
    }

	val movementButtons = MovementUtils()

	LaunchedEffect(Unit) {
		connectionManager.sendChar(velocity.velocityChar)
	}
    LaunchedEffect(modeSelected) {
        connectionManager.sendChar(
            when (modeSelected) {
                Modes.MANUAL -> modes.modeManualChar
                Modes.AUTOMATA -> modes.modeAutomataChar
            }
        )
    }
    LaunchedEffect(connectionManager.message) {
		connectionManager.message?.let {
			snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Indefinite)
		}
	}
    SetOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, LocalContext.current)

    Scaffold(
        topBar = {
            TopBarForMainPage(
                bluetoothAdapter = bluetoothAdapter,
                connectionManager = connectionManager,
                navController = navController,
                viewModel = viewModel,
                modeSelected = { modeSelected = it },
                directionsConfig = directions
            )
        },
        snackbarHost = {
			SnackbarHost(
				snackbarHostState,
				modifier = Modifier
					.wrapContentWidth()
					.wrapContentHeight()
			) { data ->
				CustomSnackbar(data)
			}
		},
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(
            Modifier
				.padding(padding)
				.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            movementButtons.GridButton(
                connectionManager = connectionManager,
				directionChars = directions,
				viewModel = viewModel
            )
        }
    }
}

