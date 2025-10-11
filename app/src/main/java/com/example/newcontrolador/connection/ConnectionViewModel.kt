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

	// indica si se está usando bluetooth o wifi
	var isBluetooth by mutableStateOf(true)

	// mensajes de error y de cumplimiento? (no sé escribir)
	var message by mutableStateOf<String?>(null)
		private set // solo se puede cambiar su valor aca dentro

	//* Bluetooth
	/**
	 * Conect to bluetoth: se le pasa la informacion de un dispocitivo bluetooth,
	 * e intenta conectarse a ese dispositivo.
	 *
	 * @param device dispositivo bluetooth al que se quiere conectar
	 * @param context contexto de la aplicacion al momento de la conexion. Se usa para
	 * verificar permisos.
	 */
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
			val permiso = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) "BLUETOOTH_CONNECT" else "BLUETOOTH"

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

	/**
	 * Send bluetooth char: envia un caracter por bluetooth al dispositivo conectado.
	 *
	 * @param char caracter a enviar
	 */
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

	/**
	 * Listen for bluetooth messages: comienza a escuchar mensajes entrantes. Se usa para detectar
	 * mensajes entrantes por controles bluetooth para despues traducirlos a comandos de movimiento
	 * para el robot conectado.
	 */
	fun listenForBluetoothMessages() {
		try {
			bluetoothConnectionManager.listenForAllDevices()
		} catch (e: BluetoothReadException) {
			message = e.message
		} catch (_: Exception) {
			message = "Error desconocido"
		}
	}

	/**
	 * Prove bluetooth devices: verifica si el conjunto de dispositivos bluetooth tiene dispocitivos
	 * o esta vacio
	 *
	 * @param setOfDevices set de dispocitivos bluetooth a verificar
	 * @return retorna un boolean true si la lista tiene dispocitivos y false en caso contrario
	 */
	fun proveBluetoothDevices(setOfDevices: Set<BluetoothDevice>): Boolean {
		if (setOfDevices.isEmpty()){
			message = "No hay dispositivos Bluetooth disponibles"
			return false
		}
		return true
	}

	//* wifi
	/**
	 * Conect to wifi: resivé una ip y se intenta conectar a la misama
	 *
	 * @param ip ip en formato string a la que se esta intentando conectar
	 */
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

	/**
	 * Send wifi char: se resive un caracter y se lo envia a el dispocitivo al que se conecto por ip
	 *
	 * @param char caracter a enviar
	 */
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
	//* general
	/**
	 * Clean message: se limpia el mensaje de error
	 */
	fun cleanMessage() {
		message = null
	}

	/**
	 * Send char: se resivé un char y se envia por bluetooth o wifi dependiendo el caso
	 *
	 * @param char caracter a enviar
	 */
	fun sendChar(char: Char) {
		if (isBluetooth) {
			sendBluetoothChar(char)
		} else {
			sendWifiChar(char)
		}
	}
}