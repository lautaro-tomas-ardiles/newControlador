package com.example.newcontrolador.connection

import android.Manifest
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

	// Indica si actualmente se está usando Bluetooth o Wi-Fi.
	var isBluetooth by mutableStateOf(true)

	// Mensaje de error o de cumplimiento? (no sé escribir)
	// (solo puede modificarse dentro del ViewModel).
	var message by mutableStateOf<String?>(null)
		private set

	// * Bluetooth *
	/**
	 * Conecta a un dispositivo Bluetooth.
	 *
	 * Intenta conectarse con el dispositivo Bluetooth dado en el parametro, verificando antes
	 * que los permisos necesarios estén.
	 *
	 * @param device Dispositivo Bluetooth al que se desea conectar.
	 * @param context Contexto de la aplicación al momento de la conexión (usado para verificar permisos).
	 */
	fun connectToBluetooth(device: BluetoothDevice , context: Context) {
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
			val permissionName =
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) "BLUETOOTH_CONNECT" else "BLUETOOTH"
			message = "Permiso $permissionName denegado"
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
	 * Envía un carácter por Bluetooth al dispositivo conectado.
	 *
	 * @param char Carácter a enviar.
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
	 * Comienza a escuchar mensajes entrantes por Bluetooth.
	 *
	 * Esta funcion se usa para detectar mensajes recibidos desde controles Bluetooth
	 * y traducirlos en comandos de movimiento para el robot conectado.
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
	 * Verifica si el conjunto de dispositivos Bluetooth disponibles no está vacío.
	 *
	 * @param setOfDevices Conjunto de dispositivos Bluetooth a verificar.
	 * @return `true` si hay dispositivos disponibles, `false` en caso contrario.
	 */
	fun verifyBluetoothDevices(setOfDevices: Set<BluetoothDevice>): Boolean {
		if (setOfDevices.isEmpty()) {
			message = "No hay dispositivos Bluetooth disponibles"
			return false
		}
		return true
	}

	// * Wi-Fi *
	/**
	 * Conecta a un dispositivo mediante Wi-Fi usando una dirección IP.
	 *
	 * @param ip Dirección IP a la que se intentará conectar.
	 */
	fun connectToWifi(ip: String) {
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
	 * Envía un carácter por Wi-Fi al dispositivo conectado.
	 *
	 * @param char Carácter a enviar.
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

	// * General *
	/**
	 * Limpia el mensaje actual de error o confirmación.
	 */
	fun cleanMessage() {
		message = null
	}

	/**
	 * Envía un carácter por Bluetooth o Wi-Fi según la conexión activa.
	 *
	 * @param char Carácter a enviar.
	 */
	fun sendChar(char: Char) {
		if (isBluetooth) {
			sendBluetoothChar(char)
		} else {
			sendWifiChar(char)
		}
	}
}
