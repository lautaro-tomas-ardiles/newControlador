package com.example.newcontrolador.utilitis

import android.widget.Toast
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.newcontrolador.connection.WiFiConnectionManager
import com.example.newcontrolador.ui.theme.Black
import com.example.newcontrolador.ui.theme.DarkYellow

@Composable
fun WifiTextField(
	wifiManager: WiFiConnectionManager,
	ip: String,
	onIpChange: (String) -> Unit
) {
	val context = LocalContext.current

	TextField(
		value = ip,
		onValueChange = { onIpChange(it) },
		label = {
			Text("ingrese la IP", color = Black)
		},
		trailingIcon = {
			IconsButtons(
				onClick = {
					val connectIp = wifiManager.connectToIp(ip, context)
					if (connectIp) {
						Toast.makeText(
							context,
							"Conectado a $ip",
							Toast.LENGTH_SHORT
						).show()
					} else {
						Toast.makeText(
							context,
							"No se pudo conectar a $ip",
							Toast.LENGTH_SHORT
						).show()
					}
				},
				tintColor = Black,
				imageVector = Icons.AutoMirrored.Filled.Send
			)
		},
		singleLine = true,
		colors = OutlinedTextFieldDefaults.colors(
			focusedContainerColor = DarkYellow,
			unfocusedContainerColor = DarkYellow,
			focusedTrailingIconColor = Black,
			unfocusedTrailingIconColor = Black,
			focusedTextColor = Black,
			unfocusedTextColor = Black
		),
		modifier = Modifier.wrapContentSize()
	)
}
