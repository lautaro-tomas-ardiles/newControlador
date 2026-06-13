package com.example.newcontrolador.utilitis

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.newcontrolador.R

class ButtonsUtils(
	private val sizeButton: Int,
	private val sizeIcon: Int
) {
	/**
	 * Devuelve el color para el borde o el fondo dependiende de un booleanno,
	 * si es `true` devuelve el color secundario del tema, si es `false` devuelve transparente
	 *
	 * @param key booleano que indica si se debe usar el color secundario o transparente
	 * @return Color correspondiente al estado del botón (sólido o con borde)
	 */
	@Composable
	private fun getSolidAndBorder(key: Boolean): Color {
		return when (key) {
			true -> MaterialTheme.colorScheme.secondary
			false -> Color.Transparent
		}
	}

	/**
	 * boton con icono Painter personalizable.
	 *
	 * @param onClick acción que se ejecuta al presionar el botón
	 * @param solid indica si tiene o no fondo sólido
	 * @param border indica si tiene o no borde
	 * @param tintColor color del ícono
	 * @param image ícono a mostrar, por defecto es un ícono de enlace externo
	 */
	@Composable
	fun Painter(
		onClick: () -> Unit,
		modifier: Modifier = Modifier,
		solid: Boolean = false,
		border: Boolean = false,
		tintColor: Color = MaterialTheme.colorScheme.tertiary,
		image: Painter = painterResource(R.drawable.external_link)
	) {
		IconButton(
			onClick = { onClick() },
			colors = IconButtonDefaults.iconButtonColors(
				containerColor = getSolidAndBorder(solid)
			),
			modifier = Modifier
				.size(sizeButton.dp)
				.border(
					width = 3.dp,
					color = getSolidAndBorder(border),
					shape = CircleShape
				)
		) {
			Icon(
				painter = image,
				contentDescription = "Ícono de acción",
				tint = tintColor,
				modifier = modifier.size(sizeIcon.dp)
			)
		}
	}

	/**
	 * Botón con icono ImageVector personalizable.
	 *
	 * @param onClick acción que se ejecuta al presionar el botón
	 * @param solid indica si el botón tiene fondo sólido o no
	 * @param border indica si el botón tiene borde o no
	 * @param tintColor color del ícono
	 * @param image ícono a mostrar, por defecto es el ícono de configuración
	 */
	@Composable
	fun ImageVector(
		onClick: () -> Unit,
		solid: Boolean = false,
		border: Boolean = false,
		tintColor: Color = MaterialTheme.colorScheme.tertiary,
		image: ImageVector = Icons.Default.Settings
	) {
		IconButton(
			onClick = { onClick() },
			colors = IconButtonDefaults.iconButtonColors(
				containerColor = getSolidAndBorder(solid)
			),
			modifier = Modifier
				.size(sizeButton.dp)
				.border(
					width = 3.dp,
					color = getSolidAndBorder(border),
					shape = CircleShape
				)
		) {
			Icon(
				imageVector = image,
				contentDescription = "Ícono de acción",
				tint = tintColor,
				modifier = Modifier.size(sizeIcon.dp)
			)
		}
	}

	/**
	 * boton con icono de Bluetooth.
	 *
	 * @param onClick acción que se ejecuta al presionar el botón
	 */
	@Composable
	fun Bluetooth(
		onClick: () -> Unit
	) {
		IconButton(
			onClick = { onClick() },
			colors = IconButtonDefaults.iconButtonColors(
				containerColor = MaterialTheme.colorScheme.secondary,
			),
			modifier = Modifier.size(sizeButton.dp)
		) {
			Icon(
				painter = painterResource(R.drawable.bluetooth),
				contentDescription = "Ícono de Bluetooth",
				tint = MaterialTheme.colorScheme.background
			)
		}
	}

	/**
	 * Texto con botón personalizado.
	 *
	 * @param text Texto que se muestra junto al botón
	 * @param button Función que recibe el objeto `ButtonsUtils` para mostrar un botón personalizado junto al texto
	 */
	@Composable
	fun Text(
		text: String,
		button: @Composable (ButtonsUtils) -> Unit
	) {
		Row (verticalAlignment = Alignment.CenterVertically) {
			Text(
				text = text,
				color = MaterialTheme.colorScheme.background,
				fontSize = MaterialTheme.typography.bodyMedium.fontSize
			)
			Spacer(modifier = Modifier.padding(3.dp))

			button(this@ButtonsUtils)
		}
	}

	/**
	 * bootn simple con texto.
	 *
	 * @param text Texto que se muestra en el botón
	 * @param onClick Acción que se ejecuta al presionar el botón
	 */
	@Composable
	fun Simple(
		text: String,
		onClick: () -> Unit
	) {
		Button(
			onClick = { onClick() },
			colors = ButtonDefaults.buttonColors(
				containerColor = MaterialTheme.colorScheme.secondary
			),
			shape = RoundedCornerShape(30)
		) {
			Text(
				text = text,
				color = MaterialTheme.colorScheme.background
			)
		}
	}
}