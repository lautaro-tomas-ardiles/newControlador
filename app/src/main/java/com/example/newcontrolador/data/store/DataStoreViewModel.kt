package com.example.newcontrolador.data.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newcontrolador.connection.data.ButtonsConfig
import com.example.newcontrolador.connection.data.DirectionsConfig
import com.example.newcontrolador.connection.data.DirectionsEnum
import com.example.newcontrolador.connection.data.VelocityConfig
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DataStoreViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {

	object DefaultConfigs {
		val directions = DirectionsConfig(
			upChar = 'F', downChar = 'B',
			leftChar = 'L', rightChar = 'R',
			upLeftChar = 'G', upRightChar = 'I',
			downLeftChar = 'H', downRightChar = 'J',
			stopChar = 'S'
		)
	}

	// Button
	val buttonConfig: StateFlow<ButtonsConfig> = dataStoreManager.loadButtonConfig
		.stateIn(viewModelScope, SharingStarted.Eagerly, ButtonsConfig())

	fun setButtonWidth(width: Float) = viewModelScope.launch {
		dataStoreManager.saveButtonWidth(width)
	}

	fun setButtonHeight(height: Float) = viewModelScope.launch {
		dataStoreManager.saveButtonHeight(height)
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

	// Velocity
	val velocityChar: StateFlow<VelocityConfig> = dataStoreManager.loadVelocityChar
		.stateIn(viewModelScope, SharingStarted.Eagerly, VelocityConfig())

	fun setVelocityChar(char: Char) = viewModelScope.launch {
		dataStoreManager.saveVelocityChar(char)
	}
}