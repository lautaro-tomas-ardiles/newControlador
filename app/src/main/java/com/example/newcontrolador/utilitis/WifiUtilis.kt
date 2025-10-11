package com.example.newcontrolador.utilitis

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.newcontrolador.connection.ConnectionViewModel
import com.example.newcontrolador.ui.theme.Black
import com.example.newcontrolador.ui.theme.DarkYellow

@Composable
fun WifiTextField(
	connectionManager: ConnectionViewModel,
	ip: String,
	onIpChange: (String) -> Unit
) {
	TextField(
		value = ip,
		onValueChange = { onIpChange(it) },
		label = {
			Text("ingrese la IP", color = Black)
		},
		trailingIcon = {
			IconsButtonsCustom(
				onClick = {
					connectionManager.connectToWifi(ip)
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
