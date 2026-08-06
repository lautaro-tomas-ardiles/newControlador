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
import androidx.lifecycle.viewModelScope
import com.example.newcontrolador.data.configs.DirectionsConfig
import com.example.newcontrolador.exceptions.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ConnectionViewModel(
	private val bluetoothConnectionManager: BluetoothConnectionManager,
	private val wifiConnectionManager: WiFiConnectionManager
) : ViewModel() {

	//* Indica si actualmente se está usando Bluetooth o Wi-Fi.
	var isBluetooth by mutableStateOf(true)

	//* Mensaje de error o de ¿cumplimiento? (no sé escribir)
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
	fun connectToBluetooth(device: BluetoothDevice, context: Context) {
		viewModelScope.launch(Dispatchers.IO) {
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

				showTempMessage("Permiso $permissionName denegado")
				return@launch
			}
			message = "Conectando ..."

			try {
				bluetoothConnectionManager.connectToDevice(device, context)

				cleanMessage()
				showTempMessage("Conectado a ${device.name ?: device.address ?: "Dispositivo desconocido"}")
			} catch (e: BluetoothSecurityException) {
				cleanMessage()
				showTempMessage(e.message ?: "Error desconocido")
			} catch (e: BluetoothConnectionFailedException) {
				cleanMessage()
				showTempMessage(e.message ?: "Error desconocido")
			} catch (e: BluetoothPermissionException) {
				cleanMessage()
				showTempMessage(e.message ?: "Error desconocido")
			} catch (_: Exception) {
				cleanMessage()
				showTempMessage("Error desconocido")
			}
		}
	}

	/**
	 * Envía un carácter por Bluetooth al dispositivo conectado.
	 *
	 * @param char Carácter a enviar.
	 */
	private fun sendBluetoothChar(char: Char) {
		viewModelScope.launch(Dispatchers.IO) {
			try {
				bluetoothConnectionManager.sendCharBluetooth(char)
			} catch (e: BluetoothDeviceNotFoundException) {
				//showTempMessage(e.message ?: "Error desconocido")
			} catch (e: BluetoothSendFailedException) {
				//showTempMessage(e.message ?: "Error desconocido")
			} catch (_: Exception) {
				showTempMessage("Error desconocido")
			}
		}
	}

	/**
	 * Comienza a escuchar mensajes entrantes por Bluetooth.
	 *
	 * Esta funcion se usa para detectar mensajes recibidos desde controles Bluetooth
	 * y traducirlos en comandos de movimiento para el robot conectado.
	 */
	fun listenForBluetoothMessages(directionsConfig: DirectionsConfig) {
		viewModelScope.launch(Dispatchers.IO) {
			try {
				bluetoothConnectionManager.listenForAllDevices(directionsConfig)
			} catch (e: BluetoothReadException) {
				showTempMessage(e.message ?: "Error desconocido")
			} catch (_: Exception) {
				showTempMessage("Error desconocido")
			}
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
			showTempMessage("No hay dispositivos Bluetooth disponibles")
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
		viewModelScope.launch(Dispatchers.IO) {
			try {
				wifiConnectionManager.connectToIp(ip)
				showTempMessage("Conectado a $ip")
			} catch (e: ConnectionTimeoutException) {
				showTempMessage(e.message ?: "Error desconocido")
			} catch (e: DeviceNotFoundException) {
				showTempMessage(e.message ?: "Error desconocido")
			} catch (e: ConnectionFailedException) {
				showTempMessage(e.message ?: "Error desconocido")
			} catch (e: UnexpectedResponseException) {
				showTempMessage(e.message ?: "Error desconocido")
			} catch (e: InvalidIpException) {
				showTempMessage(e.message ?: "Error desconocido")
			} catch (_: Exception) {
				showTempMessage("Error desconocido")
			}
		}
	}

	/**
	 * Envía un carácter por wifi al dispositivo conectado.
	 *
	 * @param char Carácter a enviar.
	 */
	private fun sendWifiChar(char: Char) {
		viewModelScope.launch(Dispatchers.IO) {
			try {
				wifiConnectionManager.sendCharWifi(char)
			} catch (e: SendCharFailedException) {
				showTempMessage(e.message ?: "Error desconocido")
			} catch (e: ConnectionTimeoutException) {
				showTempMessage(e.message ?: "Error desconocido")
			} catch (e: DeviceNotFoundException) {
				//showTempMessage(e.message ?: "Error desconocido")
			} catch (e: ConnectionFailedException) {
				showTempMessage(e.message ?: "Error desconocido")
			} catch (e: InvalidIpException) {
				//showTempMessage(e.message ?: "Error desconocido")
			} catch (_: Exception) {
				showTempMessage("Error desconocido")
			}
		}
	}

	// * General *
	/**
	 * Limpia el mensaje actual de error o confirmación.
	 */
	fun cleanMessage() {
		message = null
	}

	private fun showTempMessage(text: String, durationMs: Long = 2500L) {
		viewModelScope.launch {
			message = text
			delay(durationMs)
			cleanMessage()
		}
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
