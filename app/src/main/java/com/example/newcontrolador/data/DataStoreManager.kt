package com.example.newcontrolador.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.newcontrolador.connection.data.ConfigButton
import com.example.newcontrolador.connection.data.ConfigDirections
import com.example.newcontrolador.connection.data.ConfigModes
import com.example.newcontrolador.connection.data.Directions
import com.example.newcontrolador.connection.data.Modes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

class DataStoreManager(private val context: Context) {
	companion object {
		val THEME_KEY = stringPreferencesKey("theme")
		val HEIGHT_KEY = floatPreferencesKey("height")
		val WIDTH_KEY = floatPreferencesKey("width")
		val PADDING_KEY = floatPreferencesKey("padding")
		val MODE_MANUAL_KEY = stringPreferencesKey("mode_manual")
		val MODE_AUTOMATA_KEY = stringPreferencesKey("mode_automata")
		val UP_CHAR_KEY = stringPreferencesKey("up_char")
		val DOWN_CHAR_KEY = stringPreferencesKey("down_char")
		val LEFT_CHAR_KEY = stringPreferencesKey("left_char")
		val RIGHT_CHAR_KEY = stringPreferencesKey("right_char")
		val UP_LEFT_CHAR_KEY = stringPreferencesKey("up_left_char")
		val UP_RIGHT_CHAR_KEY = stringPreferencesKey("up_right_char")
		val DOWN_LEFT_CHAR_KEY = stringPreferencesKey("down_left_char")
		val DOWN_RIGHT_CHAR_KEY = stringPreferencesKey("down_right_char")
		val STOP_CHAR_KEY = stringPreferencesKey("stop_char")
	}

	//* guardar los datos de button */
	// guardar height
	suspend fun saveButtonHeight(height: Float) {
		context.dataStore.edit { preferences ->
			preferences[HEIGHT_KEY] = height
		}
	}

	// guardar width
	suspend fun saveButtonWidth(width: Float) {
		context.dataStore.edit { preferences ->
			preferences[WIDTH_KEY] = width
		}
	}

	// guardar padding
	suspend fun saveButtonPadding(padding: Float) {
		context.dataStore.edit { preferences ->
			preferences[PADDING_KEY] = padding
		}
	}

	//* cargar los datos de button */
	val loadButtonConfig: Flow<ConfigButton> = context.dataStore.data.map { prefs ->
		ConfigButton(
			width = prefs[WIDTH_KEY] ?: 165f,
			height = prefs[HEIGHT_KEY] ?: 150f,
			padding = prefs[PADDING_KEY] ?: 0f
		)
	}

	//* guardar y cargar los datos de direcciones y modos */
	suspend fun saveDirectionChar(key: Directions, char: Char) {
		val preferencesKey = when (key) {
			Directions.UP -> UP_CHAR_KEY
			Directions.DOWN -> DOWN_CHAR_KEY
			Directions.LEFT -> LEFT_CHAR_KEY
			Directions.RIGHT -> RIGHT_CHAR_KEY
			Directions.UP_LEFT -> UP_LEFT_CHAR_KEY
			Directions.UP_RIGHT -> UP_RIGHT_CHAR_KEY
			Directions.DOWN_LEFT -> DOWN_LEFT_CHAR_KEY
			Directions.DOWN_RIGHT -> DOWN_RIGHT_CHAR_KEY
			Directions.STOP -> STOP_CHAR_KEY
		}
		context.dataStore.edit { preferences ->
			preferences[preferencesKey] = char.toString()
		}
	}

	suspend fun saveModeChar(key: Modes, char: Char) {
		val preferencesKey = when (key) {
			Modes.MANUAL -> MODE_MANUAL_KEY
			Modes.AUTOMATA -> MODE_AUTOMATA_KEY
		}

		context.dataStore.edit { preferences ->
			preferences[preferencesKey] = char.toString()
		}
	}

	val loadDirectionChars: Flow<ConfigDirections> = context.dataStore.data.map { prefs ->
		ConfigDirections(
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

	val loadModeChars: Flow<ConfigModes> = context.dataStore.data.map { prefs ->
		ConfigModes(
			modeManualChar = (prefs[MODE_MANUAL_KEY]?.get(0)) ?: 'C',
			modeAutomataChar = (prefs[MODE_AUTOMATA_KEY]?.get(0)) ?: 'A'
		)
	}

	//* guardar y cargar el tema */
	suspend fun saveTheme(theme: String) {
		context.dataStore.edit { preferences ->
			preferences[THEME_KEY] = theme
		}
	}

	val loadTheme: Flow<String> = context.dataStore.data.map { prefs ->
		prefs[THEME_KEY] ?: "DEFAULT"
	}
}