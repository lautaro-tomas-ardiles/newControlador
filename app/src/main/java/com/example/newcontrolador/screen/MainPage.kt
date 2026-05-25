package com.example.newcontrolador.screen

import android.bluetooth.BluetoothAdapter
import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newcontrolador.connection.*
import com.example.newcontrolador.connection.data.DirectionsConfig
import com.example.newcontrolador.data.store.DataStoreViewModel
import com.example.newcontrolador.utilitis.CustomSnackbar
import com.example.newcontrolador.utilitis.Header
import com.example.newcontrolador.utilitis.JointSliders
import com.example.newcontrolador.utilitis.MovementButtons
import com.example.newcontrolador.utilitis.PrincipalTopBar
import com.example.newcontrolador.utilitis.SetOrientation

@Composable
fun MainScreen(
	bluetoothAdapter: BluetoothAdapter,
	navController: NavController,
	viewModel: DataStoreViewModel
) {
	val movementButtons = MovementButtons()
	val directions by viewModel.directionChars.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val bluetoothConnectionManager = remember { BluetoothConnectionManager() }
    val connectionManager = remember { ConnectionViewModel(bluetoothConnectionManager) }

    LaunchedEffect(connectionManager.message) {
		connectionManager.message?.let {
			snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Indefinite)
		}
	}
    SetOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, LocalContext.current)

    Scaffold (
		topBar = { PrincipalTopBar(navController = navController) },
        snackbarHost = {
            SnackbarHost(
				snackbarHostState,
				modifier = Modifier
					.wrapContentWidth()
					.wrapContentHeight()
			) { data ->
				CustomSnackbar(data)
			}
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(horizontal = 25.dp, vertical = 25.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Header(connectionManager, bluetoothAdapter)

            Spacer(Modifier.padding(10.dp))

            JointSliders(true)
            JointSliders(false)

            movementButtons.GridButton(connectionManager, directions, viewModel)
        }
    }
}

