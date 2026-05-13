package com.example.newcontrolador.connection

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
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

	// * Conjunto de dispositivos encontrados durante el escaneo. *
	private val discoveredDevices = mutableSetOf<BluetoothDevice>()

	// * Estado del escaneo. *
	private var isScanning = false

	// * BroadcastReceiver para el escaneo. *
	private var discoveryReceiver: BroadcastReceiver? = null

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
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) "BLUETOOTH_CONNECT" else "BLUETOOTH"

			throw BluetoothPermissionException("Permiso: $permiso denegado")
		}

		try {
			val socket = device.createRfcommSocketToServiceRecord(defaultUuid)
			socket.connect()
			sockets[device.address] = socket
		} catch (_: SecurityException) {
			throw BluetoothSecurityException("Falta de permisos")
		} catch (_: IOException) {
			throw BluetoothConnectionFailedException("No se pudo conectar a ${device.name ?: device.address ?: "Dispositivo desconocido"}")
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
	/*
	/**
	 * Inicia el escaneo de dispositivos Bluetooth disponibles.
	 *
	 * @param context Contexto necesario para registrar el receiver y verificar permisos.
	 * @param bluetoothAdapter Adaptador Bluetooth para iniciar el escaneo.
	 * @throws BluetoothPermissionException Si no se tienen los permisos necesarios.
	 */
	@Throws(Exception::class)
	fun startDeviceScan(
		context: Context,
		bluetoothAdapter: BluetoothAdapter
	) {
		val hasBluetoothPermission =
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
				ActivityCompat.checkSelfPermission(
					context, Manifest.permission.BLUETOOTH_SCAN
				) == PackageManager.PERMISSION_GRANTED
			} else {
				ActivityCompat.checkSelfPermission(
					context, Manifest.permission.BLUETOOTH
				) == PackageManager.PERMISSION_GRANTED
			}

		val hasLocationPermission = ActivityCompat.checkSelfPermission(
			context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

		if (!hasBluetoothPermission || !hasLocationPermission) {
			throw BluetoothPermissionException("Permisos de Bluetooth y ubicación requeridos")
		}

		if (isScanning) {
			return
		}
		discoveredDevices.clear()

		// Crear y registrar receiver
		discoveryReceiver = object : BroadcastReceiver() {
			override fun onReceive(context: Context, intent: Intent) {
				when (intent.action) {
					BluetoothDevice.ACTION_FOUND -> {
						val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
						device?.let {
							discoveredDevices.add(it)
						}
					}
					BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
						isScanning = false
					}
				}
			}
		}

		val filter = IntentFilter().apply {
			addAction(BluetoothDevice.ACTION_FOUND)
			addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
		}

		context.registerReceiver(discoveryReceiver, filter)

		// Iniciar escaneo
		isScanning = true
		bluetoothAdapter.startDiscovery()
	}

	/**
	 * Detiene el escaneo de dispositivos Bluetooth.
	 *
	 * @param context Contexto necesario para desregistrar el receiver.
	 * @param bluetoothAdapter Adaptador Bluetooth para detener el escaneo.
	 */
	fun stopDeviceScan(context: Context, bluetoothAdapter: BluetoothAdapter) {
		if (isScanning) {
			if (ActivityCompat.checkSelfPermission(
					context,
					Manifest.permission.BLUETOOTH_SCAN
				) == PackageManager.PERMISSION_GRANTED
			) {
				return
			}
			bluetoothAdapter.cancelDiscovery()
			isScanning = false
		}

		discoveryReceiver?.let {
			try {
				context.unregisterReceiver(it)
			} catch (_: IllegalArgumentException) {

			}
		}
		discoveryReceiver = null
	}

	/**
	 * Obtiene el conjunto de dispositivos encontrados durante el último escaneo.
	 *
	 * @return Conjunto de dispositivos Bluetooth encontrados.
	 */
	fun getDiscoveredDevices(): Set<BluetoothDevice> {
		return discoveredDevices.toSet()
	}
	*/
	/**
	 * Verifica si actualmente se está escaneando dispositivos.
	 *
	 * @return `true` si está escaneando, `false` en caso contrario.
	 */
	fun isScanning(): Boolean {
		return isScanning
	}
}