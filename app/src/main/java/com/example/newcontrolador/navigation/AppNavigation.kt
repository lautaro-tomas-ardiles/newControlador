package com.example.newcontrolador.navigation

import android.bluetooth.BluetoothAdapter
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newcontrolador.screen.MainArduinoOneAndHC05Page
import com.example.newcontrolador.screen.MainESP32Page
import com.example.newcontrolador.screen.MainESP8622Page
import com.example.newcontrolador.screen.MainScreen
import com.example.newcontrolador.ui.theme.ThemeType

@Composable
fun AppNavigation(
	bluetoothAdapter: BluetoothAdapter,
	themeType: (ThemeType) -> Unit
) {
	val navController = rememberNavController()

	NavHost(navController, AppScreen.MainPage.route) {
		composable(
			AppScreen.MainPage.route
		) {
			MainScreen(bluetoothAdapter, navController) {
				themeType(it)
			}
		}
		composable(
			AppScreen.ESP8622Page.route
		) {
			MainESP8622Page(navController)
		}
		composable(
			AppScreen.ESP32Page.route
		) {
			MainESP32Page(navController)
		}
		composable(
			AppScreen.ArduinoOneAndHC05Page.route
		) {
			MainArduinoOneAndHC05Page(navController)
		}
	}
}