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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.newcontrolador.navigation.AppNavigation
import com.example.newcontrolador.ui.theme.NewControladorTheme
import com.example.newcontrolador.ui.theme.ThemeType
import androidx.core.content.edit

val ThemeTypeStateSaver = Saver<MutableState<ThemeType>, String>(
    save = { state -> state.value.name },
    restore = { saved ->
        mutableStateOf(ThemeType.valueOf(saved))
    }
)

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
		setContent {
			// leer tema guardado
			val prefs = this@MainActivity.getSharedPreferences("newcontrolador_prefs", MODE_PRIVATE)
			val savedName =
				prefs.getString("theme", ThemeType.DEFAULT.name) ?: ThemeType.DEFAULT.name

			// estado del tema inicializado desde prefs
			val currentThemeState = rememberSaveable(saver = ThemeTypeStateSaver) {
				mutableStateOf(ThemeType.valueOf(savedName))
			}

			NewControladorTheme(
				themeType = currentThemeState.value,
			) {
				AppNavigation(bluetoothAdapter) { themeType ->
					currentThemeState.value = themeType
					// guardar la selección para que otras pantallas/activities la puedan leer
					prefs.edit { putString("theme", themeType.name) }
				}
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
