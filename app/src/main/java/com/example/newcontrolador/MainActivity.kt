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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import com.example.newcontrolador.data.DataStoreManager
import com.example.newcontrolador.data.DataStoreViewModel
import com.example.newcontrolador.data.DataStoreViewModelFactory
import com.example.newcontrolador.navigation.AppNavigation
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
				Toast.makeText(this, "Bluetooth deshabilitado", Toast.LENGTH_SHORT).show()
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

		// Nueva forma de ocultar solo la barra de navegación
		WindowCompat.setDecorFitsSystemWindows(window, true)
		val insetsController = WindowInsetsControllerCompat(window, window.decorView)

		// Ocultar la barra de navegación
		insetsController.hide(WindowInsetsCompat.Type.navigationBars() or WindowInsetsCompat.Type.statusBars())

		// Permite que reaparezca con swipe
		insetsController.systemBarsBehavior =
			WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

		//enableEdgeToEdge()
		val viewModel =
			ViewModelProvider(
				this,
				DataStoreViewModelFactory(DataStoreManager(this))
			)[DataStoreViewModel::class.java]

		setContent {
			val theme by viewModel.theme.collectAsState()

			NewControladorTheme(themeType = theme) {
				AppNavigation(bluetoothAdapter, viewModel)
			}
		}
	}

	// Solicita el permiso necesario para usar Bluetooth según la versión de Android
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
