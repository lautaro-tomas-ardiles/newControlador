package com.example.newcontrolador

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
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
    private lateinit var takePermission: ActivityResultLauncher<String>
    private lateinit var takeResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        takePermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                takeResultLauncher.launch(intent)
            } else {
                Toast.makeText(this, "Faltan los permisos", Toast.LENGTH_SHORT).show()
            }
        }

        takeResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                //Toast.makeText(this, "Bluetooth habilitado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Bluetooth desabilitado", Toast.LENGTH_SHORT).show()
            }
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewControladorTheme {
                MainScreen(takePermission, bluetoothAdapter)
            }
        }
    }
}