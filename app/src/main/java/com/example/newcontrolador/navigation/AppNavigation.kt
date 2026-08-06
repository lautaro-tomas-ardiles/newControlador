package com.example.newcontrolador.navigation

import android.bluetooth.BluetoothAdapter
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newcontrolador.storage.DataStoreViewModel
import com.example.newcontrolador.screen.MainAboutScreen
import com.example.newcontrolador.screen.MainArduinoOneAndHC05Page
import com.example.newcontrolador.screen.MainESP32Page
import com.example.newcontrolador.screen.MainScreen
import com.example.newcontrolador.screen.MainSettingsPage

@Composable
fun AppNavigation(
	bluetoothAdapter: BluetoothAdapter,
	viewModel: DataStoreViewModel,

) {
	val navController = rememberNavController()

	NavHost(navController, AppScreen.MainPage.route) {
		composable(AppScreen.MainPage.route) {
			MainScreen(bluetoothAdapter, navController, viewModel)
		}
		composable(AppScreen.ESP32Page.route) {
			MainESP32Page(navController, viewModel)
		}
		composable(AppScreen.ArduinoOneAndHC05Page.route) {
			MainArduinoOneAndHC05Page(navController, viewModel)
		}
		composable(AppScreen.SettingsPage.route) {
			MainSettingsPage(navController, viewModel)
		}
		composable(AppScreen.AboutPage.route) {
			MainAboutScreen(navController, viewModel)
		}
	}
}