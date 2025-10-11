package com.example.newcontrolador.connection

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import com.example.newcontrolador.connection.data.Directions
import com.example.newcontrolador.exceptions.*
import java.io.IOException
import java.util.UUID

/**
 * Gestor de conexión Bluetooth que maneja conexiones con múltiples dispositivos (p. ej. HC-05 / HC-06).
 * Permite conectar, enviar datos y escuchar información desde dispositivos conectados.
 */
class BluetoothConnectionManager {

    // * Mapa de direcciones MAC a sockets Bluetooth activos. *
    private val sockets = mutableMapOf<String, BluetoothSocket>()

    // * UUID estándar para comunicación SPP (Serial Port Profile). *
    private val defaultUuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    /**
     * Intenta conectar con un dispositivo Bluetooth.
     *
     * @param device Dispositivo Bluetooth destino.
     * @param context Contexto necesario para verificar permisos.
     * @throws BluetoothPermissionException Si no se tienen los permisos necesarios.
     * @throws BluetoothSecurityException Si ocurre un error de seguridad.
     * @throws BluetoothConnectionFailedException Si no se logra conectar al dispositivo.
     */
    @Throws(Exception::class)
    fun connectToDevice(device: BluetoothDevice, context: Context) {

        // Verificación de permisos dinámica según versión de Android
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
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
					"BLUETOOTH_CONNECT"
				else
					"BLUETOOTH"

			throw BluetoothPermissionException("Permiso $permiso denegado")
		}

		try {
			val uuid = device.uuids?.firstOrNull()?.uuid ?: defaultUuid
			val socket = device.createRfcommSocketToServiceRecord(uuid)
			socket.connect()
			sockets[device.address] = socket
		} catch (_: SecurityException) {
			throw BluetoothSecurityException("Falta de permisos")
		} catch (_: IOException) {
			throw BluetoothConnectionFailedException("No se pudo conectar a  ${device.name ?: device.address ?: "Dispositivo desconocido"}")
		}
	}

	/**
	 * Envía un carácter a todos los dispositivos Bluetooth conectados.
	 *
	 * @param char Carácter a enviar.
	 * @throws BluetoothDeviceNotFoundException Si no hay dispositivos conectados.
	 * @throws BluetoothSendFailedException Si falla el envío de datos.
	 */
	@Throws(Exception::class)
	fun sendCharBluetooth(char: Char) {
		if (sockets.isEmpty()) {
			throw BluetoothDeviceNotFoundException("No hay dispositivos Bluetooth conectados")
		}
		val iterator = sockets.iterator()

		while (iterator.hasNext()) {
			val entry = iterator.next()
			try {
				entry.value.outputStream.write(char.code)
			} catch (_: IOException) {
				entry.value.close()
				iterator.remove()
				throw BluetoothSendFailedException("Error al enviar datos a ${entry.key}")
			}
		}
	}

	/**
	 * Traduce caracteres de entrada en comandos definidos por el enum [Directions].
	 */
	private fun translateChar(c: Char): Char {
		return when (c) {
			'u' -> Directions.UP.char
			'd' -> Directions.DOWN.char
			'l' -> Directions.LEFT.char
			'r' -> Directions.RIGHT.char
			'g' -> Directions.UP_LEFT.char
			'i' -> Directions.UP_RIGHT.char
			'h' -> Directions.DOWN_LEFT.char
			'j' -> Directions.DOWN_RIGHT.char
			's' -> Directions.STOP.char
			else -> Directions.STOP.char
		}
	}

    /**
     * Escucha datos entrantes de todos los dispositivos conectados en hilos separados.
     * Cada dato recibido se traduce y reenvía mediante [sendCharBluetooth].
     *
     * @throws BluetoothReadException Si ocurre un error al leer los datos.
     */
	@Throws(Exception::class)
	fun listenForAllDevices() {
		for ((_, socket) in sockets) {
			Thread {
				try {
					val input = socket.inputStream
					val buffer = ByteArray(1024)
					while (true) {
						val bytesRead = input.read(buffer)
						if (bytesRead > 0) {
							val data = String(buffer, 0, bytesRead)
							for (char in data.lowercase()) {
								val translatedChar = translateChar(char)
								if (translatedChar != ' ') {
									sendCharBluetooth(translatedChar)
								}
							}
						}
					}
				} catch (e: IOException) {
					throw BluetoothReadException("Error al leer datos: ${e.message}")
				}
			}.start()
		}
	}
}