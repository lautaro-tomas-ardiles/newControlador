package com.example.newcontrolador.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newcontrolador.connection.data.ButtonConfig
import com.example.newcontrolador.connection.data.DirectionsConfig
import com.example.newcontrolador.connection.data.ModesConfig
import com.example.newcontrolador.connection.data.Directions
import com.example.newcontrolador.connection.data.Modes
import com.example.newcontrolador.connection.data.ThemeType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DataStoreViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {

	object DefaultConfigs {
		val directions = DirectionsConfig(
			upChar = 'F', downChar = 'B', leftChar = 'L', rightChar = 'R',
			upLeftChar = 'G', upRightChar = 'I', downLeftChar = 'H',
			downRightChar = 'J', stopChar = 'S'
		)

		val modes = ModesConfig(
			modeManualChar = 'C',
			modeAutomataChar = 'A'
		)
	}

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
	val buttonConfig: StateFlow<ButtonConfig> = dataStoreManager.loadButtonConfig
		.stateIn(viewModelScope, SharingStarted.Eagerly, ButtonConfig())

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
	val directionChars: StateFlow<DirectionsConfig> = dataStoreManager.loadDirectionChars
		.stateIn(viewModelScope, SharingStarted.Eagerly, DirectionsConfig())

	fun setDirectionChar(direction: Directions, char: Char) = viewModelScope.launch {
		dataStoreManager.saveDirectionChar(direction, char)
	}

	fun resetDirectionChars() = viewModelScope.launch {
		dataStoreManager.saveAllDirectionChars(DefaultConfigs.directions)
	}

	// Modes
	val modeChars: StateFlow<ModesConfig> = dataStoreManager.loadModeChars
		.stateIn(viewModelScope, SharingStarted.Eagerly, ModesConfig())

	fun setModeChar(mode: Modes, char: Char) = viewModelScope.launch {
		dataStoreManager.saveModeChar(mode, char)
	}

	fun resetModesToDefault() = viewModelScope.launch {
		dataStoreManager.saveAllModeChars(DefaultConfigs.modes)
	}
}
