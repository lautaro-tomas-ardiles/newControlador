package com.example.newcontrolador.connection

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import com.example.newcontrolador.connection.data.ConfigDirections
import com.example.newcontrolador.exceptions.BluetoothConnectionFailedException
import com.example.newcontrolador.exceptions.BluetoothDeviceNotFoundException
import com.example.newcontrolador.exceptions.BluetoothPermissionException
import com.example.newcontrolador.exceptions.BluetoothReadException
import com.example.newcontrolador.exceptions.BluetoothSecurityException
import com.example.newcontrolador.exceptions.BluetoothSendFailedException
import com.example.newcontrolador.exceptions.ConnectionFailedException
import com.example.newcontrolador.exceptions.ConnectionTimeoutException
import com.example.newcontrolador.exceptions.DeviceNotFoundException
import com.example.newcontrolador.exceptions.InvalidIpException
import com.example.newcontrolador.exceptions.SendCharFailedException
import com.example.newcontrolador.exceptions.UnexpectedResponseException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConnectionViewModel(
	private val bluetoothConnectionManager: BluetoothConnectionManager,
	private val wifiConnectionManager: WiFiConnectionManager
) : ViewModel() {

	// Indica si actualmente se está usando Bluetooth o Wi-Fi.
	var isBluetooth by mutableStateOf(true)

	// Mensaje de error o de cumplimiento? (no sé escribir)
	// (solo puede modificarse dentro del ViewModel).
	private val _message = MutableStateFlow<String?>(null)
	val message: StateFlow<String?> = _message.asStateFlow()


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
		CoroutineScope(Dispatchers.IO).launch {
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
				withContext(Dispatchers.Main) {
					_message.value = "Permiso $permissionName denegado"
				}
				return@launch
			}

			// Iniciar animación "Conectando..."
			val connectingJob = launch(Dispatchers.Main) {
				var dots = 0
				while (isActive) {
					_message.value = "Conectando" + ".".repeat(dots)
					dots = (dots + 1) % 4 // Cicla entre 0, 1, 2, 3 puntos
					delay(500L) // cada 0.5 segundos
				}
			}

			try {
				bluetoothConnectionManager.connectToDevice(device, context)

				// Si la conexión fue exitosa, detenemos la animación
				connectingJob.cancelAndJoin()

				withContext(Dispatchers.Main) {
					_message.value =
						"Conectado a ${device.name ?: device.address ?: "Dispositivo desconocido"}"
				}
			} catch (e: BluetoothSecurityException) {
				connectingJob.cancel()
				withContext(Dispatchers.Main) { _message.value = e.message }
			} catch (e: BluetoothConnectionFailedException) {
				connectingJob.cancel()
				withContext(Dispatchers.Main) { _message.value = e.message }
			} catch (e: BluetoothPermissionException) {
				connectingJob.cancel()
				withContext(Dispatchers.Main) { _message.value = e.message }
			} catch (_: Exception) {
				connectingJob.cancel()
				withContext(Dispatchers.Main) { _message.value = "Error desconocido" }
			}
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
		} catch (_: BluetoothDeviceNotFoundException) {
			Log.e("ConnectionViewModel", "No hay dispositivos Bluetooth conectados")
		} catch (e: BluetoothSendFailedException) {
			_message.value = e.message
		} catch (_: Exception) {
			_message.value = "Error desconocido"
		}
	}

	/**
	 * Comienza a escuchar mensajes entrantes por Bluetooth.
	 *
	 * Esta funcion se usa para detectar mensajes recibidos desde controles Bluetooth
	 * y traducirlos en comandos de movimiento para el robot conectado.
	 */
	fun listenForBluetoothMessages(configDirections: ConfigDirections) {
		try {
			bluetoothConnectionManager.listenForAllDevices(configDirections)
		} catch (e: BluetoothReadException) {
			_message.value = e.message
		} catch (_: Exception) {
			_message.value = "Error desconocido"
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
			_message.value = "No hay dispositivos Bluetooth disponibles"
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
			_message.value = "Conectado a $ip"
		} catch (e: ConnectionTimeoutException) {
			_message.value = e.message
		} catch (e: DeviceNotFoundException) {
			_message.value = e.message
		} catch (e: ConnectionFailedException) {
			_message.value = e.message
		} catch (e: UnexpectedResponseException) {
			_message.value = e.message
		} catch (e: InvalidIpException) {
			_message.value = e.message
		} catch (_: Exception) {
			_message.value = "Error desconocido"
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
			_message.value = e.message
		} catch (e: ConnectionTimeoutException) {
			_message.value = e.message
		} catch (_: DeviceNotFoundException) {
			Log.e("ConnectionViewModel", "No hay dispositivo Wi-Fi conectado")
		} catch (e: ConnectionFailedException) {
			_message.value = e.message
		} catch (e: InvalidIpException) {
			_message.value = e.message
		} catch (_: Exception) {
			_message.value = "Error desconocido"
		}
	}

	// * General *
	/**
	 * Limpia el mensaje actual de error o confirmación.
	 */
	private fun cleanMessage() {
		_message.value = null
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
