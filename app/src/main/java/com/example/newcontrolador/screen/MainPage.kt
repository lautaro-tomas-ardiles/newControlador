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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newcontrolador.connection.*
import com.example.newcontrolador.connection.data.DirectionsConfig
import com.example.newcontrolador.utilitis.CustomSnackbar
import com.example.newcontrolador.utilitis.Header
import com.example.newcontrolador.utilitis.JointSliders
import com.example.newcontrolador.utilitis.MovementButtons
import com.example.newcontrolador.utilitis.SetOrientation

@Composable
fun MainScreen(
    bluetoothAdapter: BluetoothAdapter,
    navController: NavController
) {
	/*TODO: navController se va a usar despues*/
    val movementButtons = MovementButtons()
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
                .padding(35.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Header(connectionManager, bluetoothAdapter)

            Spacer(Modifier.padding(20.dp))

            JointSliders(true)
            JointSliders(false)

            movementButtons.GridButton(connectionManager, DirectionsConfig())
        }
    }
}

