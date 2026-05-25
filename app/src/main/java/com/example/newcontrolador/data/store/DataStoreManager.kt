package com.example.newcontrolador.data.store

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.newcontrolador.connection.data.ButtonsConfig
import com.example.newcontrolador.connection.data.DirectionsConfig
import com.example.newcontrolador.connection.data.DirectionsEnum
import com.example.newcontrolador.connection.data.VelocityConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

class DataStoreManager(private val context: Context) {
	companion object {
		val HEIGHT_KEY = floatPreferencesKey("height")
		val WIDTH_KEY = floatPreferencesKey("width")

		val UP_CHAR_KEY = stringPreferencesKey("up_char")
		val DOWN_CHAR_KEY = stringPreferencesKey("down_char")
		val LEFT_CHAR_KEY = stringPreferencesKey("left_char")
		val RIGHT_CHAR_KEY = stringPreferencesKey("right_char")
		val UP_LEFT_CHAR_KEY = stringPreferencesKey("up_left_char")
		val UP_RIGHT_CHAR_KEY = stringPreferencesKey("up_right_char")
		val DOWN_LEFT_CHAR_KEY = stringPreferencesKey("down_left_char")
		val DOWN_RIGHT_CHAR_KEY = stringPreferencesKey("down_right_char")
		val STOP_CHAR_KEY = stringPreferencesKey("stop_char")

		val VELOCITY_CHAR_KEY = stringPreferencesKey("velocity_char")
	}

	//* guardar los datos de button */
	suspend fun saveButtonHeight(height: Float) {
		context.dataStore.edit { preferences ->
			preferences[HEIGHT_KEY] = height
		}
	}

	suspend fun saveButtonWidth(width: Float) {
		context.dataStore.edit { preferences ->
			preferences[WIDTH_KEY] = width
		}
	}

	//* cargar los datos de button */
	val loadButtonConfig: Flow<ButtonsConfig> = context.dataStore.data.map { prefs ->
		ButtonsConfig(
			width = prefs[WIDTH_KEY] ?: 0.95f,
			height = prefs[HEIGHT_KEY] ?: 0.75f,
		)
	}

	//* guardar y cargar los datos de direcciones y modos */
	suspend fun saveDirectionChar(key: DirectionsEnum, char: Char) {
		val preferencesKey = when (key) {
			DirectionsEnum.UP -> UP_CHAR_KEY
			DirectionsEnum.DOWN -> DOWN_CHAR_KEY
			DirectionsEnum.LEFT -> LEFT_CHAR_KEY
			DirectionsEnum.RIGHT -> RIGHT_CHAR_KEY
			DirectionsEnum.UP_LEFT -> UP_LEFT_CHAR_KEY
			DirectionsEnum.UP_RIGHT -> UP_RIGHT_CHAR_KEY
			DirectionsEnum.DOWN_LEFT -> DOWN_LEFT_CHAR_KEY
			DirectionsEnum.DOWN_RIGHT -> DOWN_RIGHT_CHAR_KEY
			DirectionsEnum.STOP -> STOP_CHAR_KEY
		}
		context.dataStore.edit { preferences ->
			preferences[preferencesKey] = char.toString()
		}
	}

	suspend fun saveAllDirectionChars(directions: DirectionsConfig) {
		context.dataStore.edit { preferences ->
			preferences[UP_CHAR_KEY] = directions.upChar.toString()
			preferences[DOWN_CHAR_KEY] = directions.downChar.toString()
			preferences[LEFT_CHAR_KEY] = directions.leftChar.toString()
			preferences[RIGHT_CHAR_KEY] = directions.rightChar.toString()
			preferences[UP_LEFT_CHAR_KEY] = directions.upLeftChar.toString()
			preferences[UP_RIGHT_CHAR_KEY] = directions.upRightChar.toString()
			preferences[DOWN_LEFT_CHAR_KEY] = directions.downLeftChar.toString()
			preferences[DOWN_RIGHT_CHAR_KEY] = directions.downRightChar.toString()
			preferences[STOP_CHAR_KEY] = directions.stopChar.toString()
		}
	}

	val loadDirectionChars: Flow<DirectionsConfig> = context.dataStore.data.map { prefs ->
		DirectionsConfig(
			upChar = (prefs[UP_CHAR_KEY]?.get(0)) ?: 'F',
			downChar = (prefs[DOWN_CHAR_KEY]?.get(0)) ?: 'B',
			leftChar = (prefs[LEFT_CHAR_KEY]?.get(0)) ?: 'L',
			rightChar = (prefs[RIGHT_CHAR_KEY]?.get(0)) ?: 'R',
			upLeftChar = (prefs[UP_LEFT_CHAR_KEY]?.get(0)) ?: 'G',
			upRightChar = (prefs[UP_RIGHT_CHAR_KEY]?.get(0)) ?: 'I',
			downLeftChar = (prefs[DOWN_LEFT_CHAR_KEY]?.get(0)) ?: 'H',
			downRightChar = (prefs[DOWN_RIGHT_CHAR_KEY]?.get(0)) ?: 'J',
			stopChar = (prefs[STOP_CHAR_KEY]?.get(0)) ?: 'S'
		)
	}

	//* guardar y cargar el caracter de velocidad */
	suspend fun saveVelocityChar(char: Char) {
		context.dataStore.edit { preferences ->
			preferences[VELOCITY_CHAR_KEY] = char.toString()
		}
	}

	val loadVelocityChar: Flow<VelocityConfig> = context.dataStore.data.map { prefs ->
		VelocityConfig(
			velocityChar = (prefs[VELOCITY_CHAR_KEY]?.get(0)) ?: '5'
		)
	}
}
