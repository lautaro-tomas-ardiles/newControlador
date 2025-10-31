package com.example.newcontrolador.navigation

import android.bluetooth.BluetoothAdapter
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newcontrolador.data.DataStoreViewModel
import com.example.newcontrolador.screen.MainArduinoOneAndHC05Page
import com.example.newcontrolador.screen.MainESP32Page
import com.example.newcontrolador.screen.MainESP8622Page
import com.example.newcontrolador.screen.MainScreen
import com.example.newcontrolador.screen.MainSettingsPage

@Composable
fun AppNavigation(
	bluetoothAdapter: BluetoothAdapter,
	viewModel: DataStoreViewModel
) {
	val navController = rememberNavController()

	NavHost(navController, AppScreen.MainPage.route) {
		composable(
			route = AppScreen.MainPage.route
		) {
			MainScreen(bluetoothAdapter, navController, viewModel)
		}
		composable(
			route = AppScreen.ESP8622Page.route
		) {
			MainESP8622Page(navController)
		}
		composable(
			route = AppScreen.ESP32Page.route
		) {
			MainESP32Page(navController)
		}
		composable(
			route = AppScreen.ArduinoOneAndHC05Page.route
		) {
			MainArduinoOneAndHC05Page(navController)
		}
		composable(
			route = AppScreen.SettingsPage.route
		) {
			MainSettingsPage(navController, viewModel)
		}
	}
}