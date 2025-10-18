package com.example.newcontrolador.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newcontrolador.connection.data.ConfigButton
import com.example.newcontrolador.connection.data.ConfigDirections
import com.example.newcontrolador.connection.data.ConfigModes
import com.example.newcontrolador.connection.data.Directions
import com.example.newcontrolador.connection.data.Modes
import com.example.newcontrolador.connection.data.ThemeType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DataStoreViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {

	// Theme
	val theme: StateFlow<ThemeType> = dataStoreManager.loadTheme.map { str ->
		try {
			ThemeType.valueOf(str)
		} catch (_: Exception) {
			ThemeType.DEFAULT
		}
	}.stateIn(viewModelScope, SharingStarted.Eagerly, ThemeType.DEFAULT)

	fun setTheme(newTheme: ThemeType) {
		viewModelScope.launch {
			dataStoreManager.saveTheme(newTheme.name)
		}
	}

	// Button
	val buttonConfig: StateFlow<ConfigButton> = dataStoreManager.loadButtonConfig
		.stateIn(viewModelScope, SharingStarted.Eagerly, ConfigButton())

	fun setButtonWidth(width: Float) = viewModelScope.launch {
		dataStoreManager.saveButtonWidth(width)
	}

	fun setButtonHeight(height: Float) = viewModelScope.launch {
		dataStoreManager.saveButtonHeight(height)
	}

	fun setButtonPadding(padding: Float) = viewModelScope.launch {
		dataStoreManager.saveButtonPadding(padding)
	}

	// Directions
	val directionChars: StateFlow<ConfigDirections> = dataStoreManager.loadDirectionChars
		.stateIn(viewModelScope, SharingStarted.Eagerly, ConfigDirections())

	fun setDirectionChar(direction: Directions, char: Char) = viewModelScope.launch {
		dataStoreManager.saveDirectionChar(direction, char)
	}

	// Modes
	val modeChars: StateFlow<ConfigModes> = dataStoreManager.loadModeChars
		.stateIn(viewModelScope, SharingStarted.Eagerly, ConfigModes())

	fun setModeChar(mode: Modes, char: Char) = viewModelScope.launch {
		dataStoreManager.saveModeChar(mode, char)
	}
}
