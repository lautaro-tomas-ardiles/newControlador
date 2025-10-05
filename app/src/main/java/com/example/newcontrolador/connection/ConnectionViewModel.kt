package com.example.newcontrolador.connection

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import com.example.newcontrolador.exceptions.*

class ConnectionViewModel (
	private val bluetoothConnectionManager: BluetoothConnectionManager,
    private val wifiConnectionManager: WiFiConnectionManager
) : ViewModel() {

	var isBluetooth by mutableStateOf(true)

	var message by mutableStateOf<String?>(null)
		private set // solo se puede cambiar su valor aca dentro

	fun sendChar(char: Char) {
        if (isBluetooth) {
            sendBluetoothChar(char)
        } else {
            sendWifiChar(char)
        }
    }

	//bluetooth
	@SuppressLint("MissingPermission")
	fun conectToBluetoth(device: BluetoothDevice, context: Context) {
		// Verificar permisos
		val hasPermission =
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
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

		if (!hasPermission) {
			val permiso =
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
					"BLUETOOTH_CONNECT"
				} else {
					"BLUETOOTH"
				}

			message = "Permiso $permiso denegado"
			return
		}
		try {
			bluetoothConnectionManager.connectToDevice(device, context)
			message = "Conectado a ${device.name ?: device.address ?: "Dispositivo desconocido"}"
		} catch (e: BluetoothSecurityException) {
			message = e.message
		} catch (e: BluetoothConnectionFailedException) {
			message = e.message
		} catch (e: BluetoothPermissionException) {
			message = e.message
		} catch (_: Exception) {
			message = "Error desconocido"
		}
	}

	private fun sendBluetoothChar(char: Char) {
		try {
			bluetoothConnectionManager.sendCharBluetooth(char)
		} catch (e: BluetoothDeviceNotFoundException) {
			message = e.message
		} catch (e: BluetoothSendFailedException) {
			message = e.message
		} catch (_: Exception) {
			message = "Error desconocido"
		}
	}

	fun listenForBluetoothMessages() {
		try {
			bluetoothConnectionManager.listenForAllDevices()
		} catch (e: BluetoothReadException) {
			message = e.message
		} catch (_: Exception) {
			message = "Error desconocido"
		}
	}

	//wifi
	fun conectToWifi(ip: String) {
		try {
			wifiConnectionManager.connectToIp(ip)
			message = "Conectado a $ip"
		} catch (e: ConnectionTimeoutException) {
			message = e.message
		} catch (e: DeviceNotFoundException) {
			message = e.message
		} catch (e: ConnectionFailedException) {
			message = e.message
		} catch (e: UnexpectedResponseException) {
			message = e.message
		} catch (e: InvalidIpException) {
			message = e.message
		} catch (_: Exception) {
			message = "Error desconocido"
		}
	}

	private fun sendWifiChar(char: Char) {
		try {
			wifiConnectionManager.sendCharWifi(char)
		} catch (e: SendCharFailedException) {
			message = e.message
		} catch (e: ConnectionTimeoutException) {
			message = e.message
		} catch (e: DeviceNotFoundException) {
			message = e.message
		} catch (e: ConnectionFailedException) {
			message = e.message
		} catch (e: InvalidIpException) {
			message = e.message
		} catch (_: Exception) {
			message = "Error desconocido"
		}
	}

	fun cleanMessage() {
        message = null
    }
}