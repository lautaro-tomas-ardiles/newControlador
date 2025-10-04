package com.example.newcontrolador.connection

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.util.UUID

class BluetoothConnectionManager {
	private val sockets = mutableMapOf<String, BluetoothSocket>()

	fun connectToDevice(device: BluetoothDevice, context: Context): Boolean {
		// UUID estándar para comunicación SPP con HC-05 o HC-06
		val uuidPorDefecto = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
		// Verificar permisos en Android 12+
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
			ActivityCompat.checkSelfPermission(
				context,
				Manifest.permission.BLUETOOTH_CONNECT
			) != PackageManager.PERMISSION_GRANTED
		) {
			Toast.makeText(context, "Permiso BLUETOOTH_CONNECT denegado", Toast.LENGTH_SHORT).show()
			return false
		}

		return try {
			val uuid = device.uuids?.firstOrNull()?.uuid ?: uuidPorDefecto
			val socket = device.createInsecureRfcommSocketToServiceRecord(uuid)
			socket.connect()
			sockets[device.address] = socket
			true
		} catch (e: IOException) {
			e.printStackTrace()
			false
		}
	}

	fun sendChar(char: Char, context: Context) {
		if (sockets.isEmpty()) {
			return
		}

		val iterator = sockets.iterator()
		while (iterator.hasNext()) {
			val entry = iterator.next()
			try {
				entry.value.outputStream.write(char.code)
			} catch (e: IOException) {
				e.printStackTrace()
				Toast.makeText(
					context,
					"Error al enviar datos a ${entry.key}",
					Toast.LENGTH_SHORT
				).show()
				entry.value.close()
				iterator.remove()
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

	fun listenForAllDevices(context: Context) {
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
                                    sendChar(translatedChar, context)
                                }
                            }
                        }
                    }
				} catch (e: IOException) {
					e.printStackTrace()
				}
			}.start()
		}
	}
}

