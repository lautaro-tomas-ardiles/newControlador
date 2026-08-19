package com.example.newcontrolador.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.newcontrolador.data.configs.ButtonsConfig
import com.example.newcontrolador.data.configs.ColorsConfig
import com.example.newcontrolador.data.configs.MovementConfig
import com.example.newcontrolador.data.configs.DirectionsConfig
import com.example.newcontrolador.data.configs.ModesConfig
import com.example.newcontrolador.data.enums.DirectionsEnum
import com.example.newcontrolador.data.enums.ModesEnum
import com.example.newcontrolador.data.configs.VelocityConfig
import com.example.newcontrolador.data.enums.ColorsEnum
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

class DataStoreManager(private val context: Context) {
	companion object {
		// temas
		val THEME_KEY = stringPreferencesKey("theme")

		// colors
		val PRIMARY_KEY = longPreferencesKey("primary")
		val ON_PRIMARY_KEY = longPreferencesKey("onPrimary")

		val SECONDARY_KEY = longPreferencesKey("secondary")
		val ON_SECONDARY_KEY = longPreferencesKey("onSecondary")

		val TERTIARY_KEY = longPreferencesKey("tertiary")
		val ON_TERTIARY_KEY = longPreferencesKey("onTertiary")

		val BACKGROUND_KEY = longPreferencesKey("background")
		val ON_BACKGROUND_KEY = longPreferencesKey("onBackground")


		// botones de movimiente
		val HEIGHT_KEY = floatPreferencesKey("height")
		val WIDTH_KEY = floatPreferencesKey("width")
		val PADDING_KEY = floatPreferencesKey("padding")

		// modos
		val MODE_MANUAL_KEY = stringPreferencesKey("mode_manual")
		val MODE_AUTOMATA_KEY = stringPreferencesKey("mode_automata")

		// direcciones
		val UP_CHAR_KEY = stringPreferencesKey("up_char")
		val DOWN_CHAR_KEY = stringPreferencesKey("down_char")
		val LEFT_CHAR_KEY = stringPreferencesKey("left_char")
		val RIGHT_CHAR_KEY = stringPreferencesKey("right_char")
		val UP_LEFT_CHAR_KEY = stringPreferencesKey("up_left_char")
		val UP_RIGHT_CHAR_KEY = stringPreferencesKey("up_right_char")
		val DOWN_LEFT_CHAR_KEY = stringPreferencesKey("down_left_char")
		val DOWN_RIGHT_CHAR_KEY = stringPreferencesKey("down_right_char")
		val STOP_CHAR_KEY = stringPreferencesKey("stop_char")

		// velocidad
		val VELOCITY_CHAR_KEY = stringPreferencesKey("velocity_char")

		// botones generales
		val BUTTON_KEY = intPreferencesKey("button")
		val ICON_KEY = intPreferencesKey("icon")
	}

	//* guardar y cargar los datos de button */
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

	suspend fun saveButtonPadding(padding: Float) {
		context.dataStore.edit { preferences ->
			preferences[PADDING_KEY] = padding
		}
	}

	val loadMovementConfig: Flow<MovementConfig> = context.dataStore.data.map { prefs ->
		MovementConfig(
			width = prefs[WIDTH_KEY] ?: 0.8f,
			height = prefs[HEIGHT_KEY] ?: 0.9f,
			padding = prefs[PADDING_KEY] ?: 0f
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

	suspend fun saveModeChar(key: ModesEnum, char: Char) {
		val preferencesKey = when (key) {
			ModesEnum.MANUAL -> MODE_MANUAL_KEY
			ModesEnum.AUTOMATA -> MODE_AUTOMATA_KEY
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

	suspend fun saveAllModeChars(modes: ModesConfig) {
		context.dataStore.edit { preferences ->
			preferences[MODE_MANUAL_KEY] = modes.modeManualChar.toString()
			preferences[MODE_AUTOMATA_KEY] = modes.modeAutomataChar.toString()
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

	val loadModeChars: Flow<ModesConfig> = context.dataStore.data.map { prefs ->
		ModesConfig(
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

	//* guardar y cargar los colores */
	suspend fun saveColors(color: Long, key: ColorsEnum) {
		when (key) {
			ColorsEnum.PRIMARY -> context.dataStore.edit { preferences ->
				preferences[PRIMARY_KEY] = color
			}
			ColorsEnum.ON_PRIMARY -> context.dataStore.edit { preferences ->
				preferences[ON_PRIMARY_KEY] = color
			}
			ColorsEnum.SECONDARY -> context.dataStore.edit { preferences ->
				preferences[SECONDARY_KEY] = color
			}
			ColorsEnum.ON_SECONDARY -> context.dataStore.edit { preferences ->
				preferences[ON_SECONDARY_KEY] = color
			}
			ColorsEnum.TERTIARY -> context.dataStore.edit { preferences ->
				preferences[TERTIARY_KEY] = color
			}
			ColorsEnum.ON_TERTIARY -> context.dataStore.edit { preferences ->
				preferences[ON_TERTIARY_KEY] = color
			}
			ColorsEnum.BACKGROUND -> context.dataStore.edit { preferences ->
				preferences[BACKGROUND_KEY] = color
			}
			ColorsEnum.ON_BACKGROUND -> context.dataStore.edit { preferences ->
				preferences[ON_BACKGROUND_KEY] = color
			}
		}
	}

	val loadColors: Flow<ColorsConfig> = context.dataStore.data.map { prefs ->
		ColorsConfig(
			primary = prefs[PRIMARY_KEY] ?: 0xFF000000,
			onPrimary = prefs[ON_PRIMARY_KEY] ?: 0xFF000000,
			secondary = prefs[SECONDARY_KEY] ?: 0xFF000000,
			onSecondary = prefs[ON_SECONDARY_KEY] ?: 0xFF000000,
			tertiary = prefs[TERTIARY_KEY] ?: 0xFF000000,
			onTertiary = prefs[ON_TERTIARY_KEY] ?: 0xFF000000,
			background = prefs[BACKGROUND_KEY] ?: 0xFF000000,
			onBackground = prefs[ON_BACKGROUND_KEY] ?: 0xFF000000
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

	//* guardar y cargar los datos de button utils */
	suspend fun saveIconSize(size: Int) {
		context.dataStore.edit { preferences ->
			preferences[ICON_KEY] = size
		}
	}

	suspend fun saveButtonSize(size: Int) {
		context.dataStore.edit { preferences ->
			preferences[BUTTON_KEY] = size
		}
	}

	val loadButtonsConfig: Flow<ButtonsConfig> = context.dataStore.data.map { prefs ->
		ButtonsConfig(
			icon = prefs[ICON_KEY] ?: 30,
			button = prefs[BUTTON_KEY] ?: 45
		)
	}

}