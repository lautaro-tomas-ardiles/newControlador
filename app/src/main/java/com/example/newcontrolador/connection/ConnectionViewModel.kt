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
import com.example.newcontrolador.exceptions.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ConnectionViewModel(private val bluetoothConnectionManager: BluetoothConnectionManager) : ViewModel() {

	//* Mensaje de error o de ¿cumplimiento? (no sé escribir)
	// (solo puede modificarse dentro del ViewModel).
	var message by mutableStateOf<String?>(null)
		private set

	var isScanning by mutableStateOf(false)
		private set

	//* Dispositivos encontrados *
	var discoveredDevices by mutableStateOf<Set<BluetoothDevice>>(emptySet())
		private set

	/**
	 * Conecta a un dispositivo Bluetooth.
	 *
	 * Intenta conectarse con el dispositivo Bluetooth dado en el parametro, verificando antes
	 * que los permisos necesarios estén.
	 *
	 * @param device Dispositivo Bluetooth al que se desea conectar.
	 * @param context Contexto de la aplicación al momento de la conexión (usado para verificar permisos).
	 */
	fun connect(device: BluetoothDevice, context: Context) {
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
	fun sendChar(char: Char) {
		viewModelScope.launch(Dispatchers.IO) {
			try {
				bluetoothConnectionManager.sendCharBluetooth(char)
			} catch (_: BluetoothDeviceNotFoundException) {
				//showTempMessage(e.message ?: "Error desconocido")
			} catch (_: BluetoothSendFailedException) {
				//showTempMessage(e.message ?: "Error desconocido")
			} catch (_: Exception) {
				showTempMessage("Error desconocido")
			}
		}
	}
	/*
	fun startBluetoothScan(context: Context, bluetoothAdapter: BluetoothAdapter) {
		viewModelScope.launch(Dispatchers.IO) {
			try {
				bluetoothConnectionManager.startDeviceScan(context, bluetoothAdapter)
				isScanning = true
				showTempMessage("Escaneando dispositivos...")
			} catch (e: BluetoothPermissionException) {
				showTempMessage(e.message ?: "Permisos requeridos")
			} catch (_: Exception) {
				showTempMessage("Error al iniciar escaneo")
			}
		}
	}

	fun stopBluetoothScan(context: Context, bluetoothAdapter: BluetoothAdapter) {
		viewModelScope.launch(Dispatchers.IO) {
			bluetoothConnectionManager.stopDeviceScan(context, bluetoothAdapter)
			isScanning = false
			discoveredDevices = bluetoothConnectionManager.getDiscoveredDevices()
			showTempMessage("Escaneo completado")
		}
	}

	fun getDevices(): Set<BluetoothDevice> {
		return bluetoothConnectionManager.getDiscoveredDevices()
	}
	*/
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
}
