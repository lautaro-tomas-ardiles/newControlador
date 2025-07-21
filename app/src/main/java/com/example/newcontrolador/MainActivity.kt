package com.example.newcontrolador

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.newcontrolador.screen.MainScreen
import com.example.newcontrolador.ui.theme.NewControladorTheme

class MainActivity : ComponentActivity() {
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var enableBtLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        //revisa si el bluetooth está habilitado
        enableBtLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode != RESULT_OK) {
                Toast.makeText(this, "Bluetooth desabilitado", Toast.LENGTH_SHORT).show()
            }
        }

        //revisa los permisos de bluetooth
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                checkAndEnableBluetooth()
            } else {
                Toast.makeText(this, "Faltan los permisos", Toast.LENGTH_SHORT).show()
            }
        }

        // Solicita el permission de Bluetooth
        requestBluetoothPermission()

        // codigo deprecate
        window.decorView.systemUiVisibility = (
                android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
                )

        enableEdgeToEdge()
        setContent {
            NewControladorTheme (darkTheme = true) {
                MainScreen(bluetoothAdapter)
            }
        }
    }

    // Solicita el permission necesario para usar Bluetooth según la versión de Android
    private fun requestBluetoothPermission() {
        val permission =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Manifest.permission.BLUETOOTH_CONNECT
            } else {
                Manifest.permission.BLUETOOTH
            }
        permissionLauncher.launch(permission)
    }

    // Verifica si el Bluetooth está habilitado y, si no, solicita al usuario que lo active
    private fun checkAndEnableBluetooth() {
        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            enableBtLauncher.launch(enableBtIntent)
        }
    }
}