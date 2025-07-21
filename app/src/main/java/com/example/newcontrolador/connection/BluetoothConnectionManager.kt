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
        val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

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
            val socket = device.createRfcommSocketToServiceRecord(uuid)
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
            Toast.makeText(context, "No hay conexiones Bluetooth", Toast.LENGTH_SHORT).show()
            return
        }

        val iterator = sockets.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            try {
                entry.value.outputStream.write(char.code)
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(context, "Error al enviar datos a ${entry.key}", Toast.LENGTH_SHORT).show()
                try {
                    entry.value.close()
                } catch (_: IOException) {}
                iterator.remove()
            }
        }
    }

}

