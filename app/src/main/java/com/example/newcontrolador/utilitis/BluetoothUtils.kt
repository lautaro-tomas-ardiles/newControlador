package com.example.newcontrolador.utilitis

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.newcontrolador.R
import com.example.newcontrolador.connection.ConnectionViewModel
import com.example.newcontrolador.ui.theme.Black
import com.example.newcontrolador.ui.theme.DarkGreen
import com.example.newcontrolador.ui.theme.DarkYellow
import com.example.newcontrolador.ui.theme.LightGreen
import com.example.newcontrolador.ui.theme.LightYellow

/**
 * Menú desplegable de dispositivos Bluetooth.
 *
 * Muestra un menú con la lista de dispositivos Bluetooth disponibles para conexión.
 * Al seleccionar un dispositivo, se intenta establecer la conexión.
 *
 * @param state Estado del menú. Si es `true`, el menú se muestra; de lo contrario, se oculta.
 * @param onStateChange cambio de estado del menu. Cambia el estado del menu si se preciona fuera
 * del menu o si se termina la conecxion con el dispocitivo.
 * @param setOfDevices Conjunto de dispositivos Bluetooth disponibles para conexión.
 * @param connectionManager ViewModel encargado de gestionar la conexión.
 * @param context el contexto de la aplicacion en el momento dado.
 */
@Composable
fun BluetoothDropMenu(
	state: Boolean,
	onStateChange: (Boolean) -> Unit,
	setOfDevices: Set<BluetoothDevice>,
	connectionManager: ConnectionViewModel,
	context: Context
) {
	DropdownMenu(
		expanded = state,
		onDismissRequest = { onStateChange(false) },
		modifier = Modifier
			.background(DarkGreen)
			.wrapContentSize()
	) {
		setOfDevices.forEach { device ->
			DeviceItem(device) {
				connectionManager.connectToBluetooth(device, context)
				connectionManager.listenForBluetoothMessages()
				onStateChange(false)
			}
		}
	}
}

/**
 * Elemento visual de dispositivo Bluetooth dentro del menú.
 *
 * Muestra el nombre y dirección del dispositivo Bluetooth. Si se presiona, ejecuta la acción indicada.
 *
 * @param device Dispositivo Bluetooth a mostrar.
 * @param onClick Acción a ejecutar al presionar el elemento.
 */
@Composable
private fun DeviceItem(
	device: BluetoothDevice,
	onClick: () -> Unit
) {
	val context = LocalContext.current
	val hasPermission =
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
			ActivityCompat.checkSelfPermission(
				context,
				Manifest.permission.BLUETOOTH_CONNECT
			) == PackageManager.PERMISSION_GRANTED
		} else {
			ActivityCompat.checkSelfPermission(
				context,
				Manifest.permission.BLUETOOTH
			) == PackageManager.PERMISSION_GRANTED
		}

	val deviceName = if (hasPermission) {
		device.name ?: "Dispositivo desconocido"
	} else {
		"Permiso requerido"
	}

	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(8.dp)
			.wrapContentWidth()
			.clickable { onClick() },
		elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
		colors = CardDefaults.cardColors(containerColor = DarkYellow)
	) {
		Column(
			Modifier.padding(16.dp)
		) {
			Text(
				text = deviceName,
				color = Black
			)
			Text(
				text = device.address,
				color = DarkGreen
			)
		}
	}
}

/**
 * Interruptor para seleccionar el modo de conexión (Bluetooth o Wi-Fi).
 *
 * Muestra un `Switch` que cambia entre Bluetooth y Wi-Fi, mostrando un ícono representativo.
 *
 * @param bluetooth Estado actual del interruptor (`true` si Bluetooth está activo).
 * @param onBluetoothChange Función que se ejecuta al cambiar el valor del interruptor.
 */
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