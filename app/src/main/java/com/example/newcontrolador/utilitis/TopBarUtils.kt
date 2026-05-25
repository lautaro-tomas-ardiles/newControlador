package com.example.newcontrolador.utilitis

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.newcontrolador.R
import com.example.newcontrolador.navigation.AppScreen
import com.example.newcontrolador.ui.theme.NewControladorTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrincipalTopBar(navController: NavController) {
	val uriHandler = LocalUriHandler.current

	TopAppBar(
		title = { Text("Control de E-BOOT") },
		actions = {
			IconButton(
				onClick = { uriHandler.openUri("https://www.instagram.com/e_boott_26ok/") }
			) {
				Icon(
					painter = painterResource(R.drawable.instagram_logo),
					contentDescription = "Settings",
					tint = MaterialTheme.colorScheme.secondary
				)
			}

			IconButton(
				onClick = { navController.navigate(AppScreen.SettingsPage.route) }
			) {
				Icon(
					imageVector = Icons.Default.Settings,
					contentDescription = "Settings",
					tint = MaterialTheme.colorScheme.secondary
				)
			}
		},
		colors = TopAppBarDefaults.topAppBarColors(
			containerColor = MaterialTheme.colorScheme.primary,
			titleContentColor = MaterialTheme.colorScheme.background,
			navigationIconContentColor = MaterialTheme.colorScheme.secondary
		)
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar(
	title: String,
	navController: NavController
) {
	TopAppBar(
		title = { Text(title) },
		navigationIcon = {
			IconButton(
				onClick = { navController.popBackStack() }
			) {
				Icon(
					imageVector = Icons.AutoMirrored.Filled.ArrowBack,
					contentDescription = "Back",
					tint = MaterialTheme.colorScheme.secondary
				)
			}
		},
		colors = TopAppBarDefaults.topAppBarColors(
			containerColor = MaterialTheme.colorScheme.primary,
			titleContentColor = MaterialTheme.colorScheme.background,
			navigationIconContentColor = MaterialTheme.colorScheme.secondary
		)
	)
}

@Preview
@Composable
private fun TopBarPrev() {
	NewControladorTheme(darkTheme = true) {
		Column {
			SettingsTopBar("Settings", NavController(LocalContext.current))
			PrincipalTopBar(NavController(LocalContext.current))
		}
	}
}