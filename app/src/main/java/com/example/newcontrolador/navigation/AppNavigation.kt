package com.example.newcontrolador.navigation

import android.bluetooth.BluetoothAdapter
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newcontrolador.data.store.DataStoreViewModel
import com.example.newcontrolador.screen.MainScreen
import com.example.newcontrolador.screen.MainSettingsPage

@Composable
fun AppNavigation(bluetoothAdapter: BluetoothAdapter, viewModel: DataStoreViewModel) {
	val navController = rememberNavController()

	NavHost(navController, AppScreen.MainPage.route) {
		composable(AppScreen.MainPage.route) {
			MainScreen(bluetoothAdapter, navController, viewModel)
		}
		composable(AppScreen.SettingsPage.route) {
			MainSettingsPage(navController, viewModel)
		}
	}
}