package com.example.newcontrolador.storage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newcontrolador.data.configs.ButtonsConfig
import com.example.newcontrolador.data.configs.ColorsConfig
import com.example.newcontrolador.data.configs.MovementConfig
import com.example.newcontrolador.data.configs.DirectionsConfig
import com.example.newcontrolador.data.configs.ModesConfig
import com.example.newcontrolador.data.enums.DirectionsEnum
import com.example.newcontrolador.data.enums.ModesEnum
import com.example.newcontrolador.data.enums.ThemeType
import com.example.newcontrolador.data.configs.VelocityConfig
import com.example.newcontrolador.data.enums.ColorsEnum
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DataStoreViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {

	object DefaultConfigs {
		val colors = ColorsConfig(
			primary = 0xFF008DD5,
			secondary = 0xFFFBD552,
			onSecondary = 0xFFDEB93B,
			tertiary = 0xFF00ECBA,
			onTertiary = 0xFF009071,
			background = 0xFF202C39,
			onBackground = 0xFF222222
		)

		val directions = DirectionsConfig(
			upChar = 'F', downChar = 'B',
			leftChar = 'L', rightChar = 'R',
			upLeftChar = 'G', upRightChar = 'I',
			downLeftChar = 'H', downRightChar = 'J',
			stopChar = 'S'
		)

		val modes = ModesConfig(
			modeManualChar = 'C',
			modeAutomataChar = 'A'
		)

		val movement = MovementConfig(
			width = 0.8f,
			height = 0.9f,
			padding = 0f
		)

		val buttons = ButtonsConfig(
			button = 45,
			icon = 30
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

	// Movement
	val movementConfig: StateFlow<MovementConfig> = dataStoreManager.loadMovementConfig
		.stateIn(viewModelScope, SharingStarted.Eagerly, MovementConfig())

	fun setButtonWidth(width: Float) = viewModelScope.launch {
		dataStoreManager.saveButtonWidth(width)
	}

	fun setButtonHeight(height: Float) = viewModelScope.launch {
		dataStoreManager.saveButtonHeight(height)
	}

	fun setButtonPadding(padding: Float) = viewModelScope.launch {
		dataStoreManager.saveButtonPadding(padding)
	}

	fun resetMovementConfig() = viewModelScope.launch {
		dataStoreManager.saveButtonHeight(DefaultConfigs.movement.height)
		dataStoreManager.saveButtonWidth(DefaultConfigs.movement.width)
		dataStoreManager.saveButtonPadding(DefaultConfigs.movement.padding)
	}

	// colors
	val colors: StateFlow<ColorsConfig> = dataStoreManager.loadColors
		.stateIn(viewModelScope, SharingStarted.Eagerly, ColorsConfig())

	fun setColors(color: Long, key: ColorsEnum) = viewModelScope.launch {
		dataStoreManager.saveColors(color, key)
	}

	// DirectionsEnum
	val directionChars: StateFlow<DirectionsConfig> = dataStoreManager.loadDirectionChars
		.stateIn(viewModelScope, SharingStarted.Eagerly, DirectionsConfig())

	fun setDirectionChar(direction: DirectionsEnum, char: Char) = viewModelScope.launch {
		dataStoreManager.saveDirectionChar(direction, char)
	}

	fun resetDirectionChars() = viewModelScope.launch {
		dataStoreManager.saveAllDirectionChars(DefaultConfigs.directions)
	}

	// ModesEnum
	val modeChars: StateFlow<ModesConfig> = dataStoreManager.loadModeChars
		.stateIn(viewModelScope, SharingStarted.Eagerly, ModesConfig())

	fun setModeChar(mode: ModesEnum, char: Char) = viewModelScope.launch {
		dataStoreManager.saveModeChar(mode, char)
	}

	fun resetModesToDefault() = viewModelScope.launch {
		dataStoreManager.saveAllModeChars(DefaultConfigs.modes)
	}

	// Velocity
	val velocityChar: StateFlow<VelocityConfig> = dataStoreManager.loadVelocityChar
		.stateIn(viewModelScope, SharingStarted.Eagerly, VelocityConfig())

	fun setVelocityChar(char: Char) = viewModelScope.launch {
		dataStoreManager.saveVelocityChar(char)
	}

	// Buttons
	val buttonsConfig: StateFlow<ButtonsConfig> = dataStoreManager.loadButtonsConfig
		.stateIn(viewModelScope, SharingStarted.Eagerly, ButtonsConfig())

	fun setButtonSize(size: Int) = viewModelScope.launch {
		dataStoreManager.saveButtonSize(size)
	}

	fun setIconSize(size: Int) = viewModelScope.launch {
		dataStoreManager.saveIconSize(size)
	}

	fun resetButtonsConfig() = viewModelScope.launch {
		dataStoreManager.saveButtonSize(DefaultConfigs.buttons.button)
		dataStoreManager.saveIconSize(DefaultConfigs.buttons.icon)
	}
}
