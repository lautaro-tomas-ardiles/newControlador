package com.example.newcontrolador.utilitis

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun CustomSnackbar(snackbarData: SnackbarData) {
	Snackbar(
		snackbarData = snackbarData,
		contentColor = MaterialTheme.colorScheme.background,
		shape = RoundedCornerShape(10.dp),
		containerColor = MaterialTheme.colorScheme.secondary
	)
}