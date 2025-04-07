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
    private var _socket: BluetoothSocket? = null

    fun connectToDevice(device: BluetoothDevice, context: Context): Boolean {
        // UUID estándar para comunicación SPP con HC-05
        val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

        // Verificar permisos en Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Permiso BLUETOOTH_CONNECT denegado", Toast.LENGTH_SHORT).show()
            return false
        }

        return try {
            _socket = device.createRfcommSocketToServiceRecord(uuid)
            _socket?.connect()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            _socket = null
            false
        }
    }

    fun sendChar(char: Char, context: Context) {
        if (_socket == null) {
            Toast.makeText(context, "No hay conexión Bluetooth", Toast.LENGTH_SHORT).show()
        }

        try {
            _socket?.outputStream?.write(char.code) // Enviar carácter como byte
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Error al enviar datos", Toast.LENGTH_SHORT).show()
        }
    }

}

