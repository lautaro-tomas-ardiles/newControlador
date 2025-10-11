package com.example.newcontrolador.connection

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import com.example.newcontrolador.exceptions.*
import java.io.IOException
import java.util.UUID

class BluetoothConnectionManager {
	private val sockets = mutableMapOf<String, BluetoothSocket>()

	@Throws(Exception::class)
	fun connectToDevice(device: BluetoothDevice, context: Context) {
		// UUID estándar para comunicación SPP con HC-05 o HC-06
		val uuidPorDefecto = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

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
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
					"BLUETOOTH_CONNECT"
				else
					"BLUETOOTH"

			throw BluetoothPermissionException("Permiso $permiso denegado")
		}

		try {
			val uuid = device.uuids?.firstOrNull()?.uuid ?: uuidPorDefecto
			val socket = device.createRfcommSocketToServiceRecord(uuid)
			socket.connect()
			sockets[device.address] = socket
		} catch (_: SecurityException) {
			throw BluetoothSecurityException("Falta de permisos")
		} catch (_: IOException) {
			throw BluetoothConnectionFailedException("No se pudo conectar a  ${device.name ?: device.address ?: "Dispositivo desconocido"}")
		}
	}

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

