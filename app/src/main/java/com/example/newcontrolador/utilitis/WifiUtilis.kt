package com.example.newcontrolador.utilitis

import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.newcontrolador.connection.ConnectionViewModel

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
			Text("ingrese la IP", color = MaterialTheme.colorScheme.background)
		},
		trailingIcon = {
			IconsButtonsCustom(
				onClick = {
					connectionManager.connectToWifi(ip)
				},
				tintColor = MaterialTheme.colorScheme.background,
				imageVector = Icons.AutoMirrored.Filled.Send
			)
		},
		singleLine = true,
		colors = OutlinedTextFieldDefaults.colors(
			focusedContainerColor = MaterialTheme.colorScheme.onSecondary,
			unfocusedContainerColor = MaterialTheme.colorScheme.onSecondary,
			focusedTrailingIconColor = MaterialTheme.colorScheme.background,
			unfocusedTrailingIconColor = MaterialTheme.colorScheme.background,
			focusedTextColor = MaterialTheme.colorScheme.background,
			unfocusedTextColor = MaterialTheme.colorScheme.background,
			errorTextColor = MaterialTheme.colorScheme.primary
		),
		modifier = Modifier.width(170.dp)
	)
}
