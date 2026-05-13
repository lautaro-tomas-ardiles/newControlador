package com.example.newcontrolador.utilitis

import android.bluetooth.BluetoothAdapter
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newcontrolador.ui.theme.NewControladorTheme
import com.example.newcontrolador.R.drawable.bluetooth
import com.example.newcontrolador.R.drawable.clamp_close
import com.example.newcontrolador.connection.ConnectionViewModel

@Composable
fun Header(connectionViewModel: ConnectionViewModel, bluetoothAdapter: BluetoothAdapter) {
	val button = Buttons()
	var isPressedLeftClamp by remember { mutableStateOf(false) }
	var isPressedRightClamp by remember { mutableStateOf(false) }

	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween,
		modifier = Modifier.fillMaxWidth()
	) {
		Label(text = "Izq", inverted = false)
		button.Toggle(
			onClick = { /*TODO: cerrar pinza*/ },
			onSecondClick = { /*TODO: lo que se manda para abrrir pinza*/ },
			isPressed = isPressedLeftClamp,
			onChangepressed = { isPressedLeftClamp = it },
			contentDescription = "Pinza Izquierda",
			imageVector = Icons.Default.KeyboardArrowDown,
			painter = painterResource(clamp_close)
		)
		button.Painter(
			painter = painterResource(bluetooth),
			inverted = true,
			contentDescription = "connecion bluetooth",
			bluetoothAdapter = bluetoothAdapter,
			connectionManager = connectionViewModel
		)
		button.ImageVector(
			onClick = { /*TODO*/ },
			imageVector = Icons.AutoMirrored.Filled.VolumeUp,
			inverted = true,
			contentDescription = "sonido"
		)
		button.Toggle(
			onClick = { /*TODO: cerrar pinza*/ },
			onSecondClick = { /*TODO: lo que se manda para abrrir pinza*/ },
			isPressed = isPressedRightClamp,
			onChangepressed = { isPressedRightClamp = it },
			contentDescription = "Pinza derecha",
			imageVector = Icons.Default.KeyboardArrowDown,
			painter = painterResource(clamp_close)
		)
		Label(text = "Der", inverted = true)
	}
}

@Composable
fun HeaderForPrev() {
	val button = Buttons()
	var isPressedLeftClamp by remember { mutableStateOf(false) }
	var isPressedRightClamp by remember { mutableStateOf(false) }

	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween,
		modifier = Modifier.fillMaxWidth()
	) {
		Label(text = "Izq", inverted = false)
		button.Toggle(
			onClick = { /*TODO: cerrar pinza*/ },
			onSecondClick = { /*TODO: lo que se manda para abrrir pinza*/ },
			isPressed = isPressedLeftClamp,
			onChangepressed = { isPressedLeftClamp = it },
			contentDescription = "Pinza Izquierda",
			imageVector = Icons.Default.KeyboardArrowDown,
			painter = painterResource(clamp_close)
		)
//		button.Painter(
//			onClick = {},
//			painter = painterResource(bluetooth),
//			inverted = true,
//			contentDescription = "connecion bluetooth"
//		)
		button.ImageVector(
			onClick = { /*TODO*/ },
			imageVector = Icons.AutoMirrored.Filled.VolumeUp,
			inverted = true,
			contentDescription = "sonido"
		)
		button.Toggle(
			onClick = { /*TODO: cerrar pinza*/ },
			onSecondClick = { /*TODO: lo que se manda para abrrir pinza*/ },
			isPressed = isPressedRightClamp,
			onChangepressed = { isPressedRightClamp = it },
			contentDescription = "Pinza derecha",
			imageVector = Icons.Default.KeyboardArrowDown,
			painter = painterResource(clamp_close)
		)
		Label(text = "Der", inverted = true)
	}
}

@Preview(device = "spec:width=411dp,height=891dp")
@Composable
private fun AllPrev() {
	NewControladorTheme {
		Scaffold(
			topBar = { }
		) { paddingValues ->
			Column(
				modifier = Modifier
					.padding(paddingValues)
					.padding(35.dp)
					.fillMaxSize(),
				verticalArrangement = Arrangement.Center,
			) {
				val movement = MovementButtons()
				HeaderForPrev()

				Spacer(Modifier.padding(20.dp))

				JointSliders(true)
				JointSliders(false)

				movement.GridButtonA()
			}
		}
	}
}

@Preview(device = "spec:width=800dp,height=1280dp")
@Composable
private fun AllTabletPrev() {
	NewControladorTheme {
		Scaffold(
			topBar = { }
		) { paddingValues ->
			Column(
				modifier = Modifier
					.padding(paddingValues)
					.padding(
						horizontal = 35.dp,
						vertical = 35.dp
					)
					.fillMaxSize(),
				verticalArrangement = Arrangement.Center,
			) {
				val movement = MovementButtons()
				HeaderForPrev()

				Spacer(Modifier.padding(20.dp))

				JointSliders(true)
				JointSliders(false)

				movement.GridButtonA()
			}
		}
	}
}

@Preview
@Composable
private fun HeaderPrev() {
	NewControladorTheme {
		HeaderForPrev()
	}
}

