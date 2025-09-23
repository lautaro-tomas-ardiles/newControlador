package com.example.newcontrolador.utilitis

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.newcontrolador.R
import com.example.newcontrolador.connection.Directions
import com.example.newcontrolador.ui.theme.Black
import com.example.newcontrolador.ui.theme.Blue
import com.example.newcontrolador.ui.theme.DarkGreen
import com.example.newcontrolador.ui.theme.DarkYellow
import com.example.newcontrolador.ui.theme.LightGreen
import com.example.newcontrolador.ui.theme.LightYellow

fun getDirectionChar(directionsPressed: Set<Directions>): Char {
	return Directions.fromSet(directionsPressed).char
}

@Composable
fun BluetoothDevices(
	pairedDevices: Set<BluetoothDevice>,
	onDeviceClick: (BluetoothDevice) -> Unit
) {
	LazyColumn(
		modifier = Modifier
			.background(color = DarkYellow)
			.fillMaxWidth(0.4f)
	) {
		items(
			items = pairedDevices.toList()
		) { device ->
			DeviceItem(
				device = device,
				onClick = { onDeviceClick(device) } // Se usa correctamente
			)
		}
	}
}

@Composable
fun DeviceItem(
	device: BluetoothDevice,
	onClick: () -> Unit
) {
	val context = LocalContext.current
	val deviceName =
		if (ActivityCompat.checkSelfPermission(
				context,
				Manifest.permission.BLUETOOTH_CONNECT
			) == PackageManager.PERMISSION_GRANTED
		) {
			device.name ?: "Dispositivo desconocido"
		} else {
			"Permiso requerido"
		}

	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(8.dp)
			.clickable { onClick() },
		elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
		colors = CardDefaults.cardColors(containerColor = Blue)
	) {
		Column(
			Modifier.padding(16.dp)
		) {
			Text(
				text = deviceName,
				color = Color.White
			)
			Text(
				text = device.address,
				color = Color.Gray
			)
		}
	}
}

@Composable
fun BluetoothSwitch(
	bluetooth: Boolean,
	onBluetoothChange: (Boolean) -> Unit
) {
	Switch(
		checked = bluetooth,
		onCheckedChange = { onBluetoothChange(it) },
		colors = SwitchDefaults.colors(
			checkedBorderColor = DarkGreen,
			checkedTrackColor = LightGreen,
			checkedThumbColor = DarkGreen,
			checkedIconColor = Black,

			uncheckedBorderColor = DarkYellow,
			uncheckedTrackColor = LightYellow,
			uncheckedThumbColor = DarkYellow,
			uncheckedIconColor = Black
		),
		thumbContent = {
			if (bluetooth) {
				Icon(
					painter = painterResource(R.drawable.group_11),
					contentDescription = "bluetooth icon",
					modifier = Modifier.size(20.dp)
				)
			} else {
				Icon(
					painter = painterResource(R.drawable.group_10),
					contentDescription = "wifi icon",
					modifier = Modifier.size(20.dp)
				)
			}
		}
	)
}