package com.example.newcontrolador.utilitis

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.newcontrolador.ui.theme.Black
import com.example.newcontrolador.ui.theme.LightYellow

@Composable
fun CustomSnackbar(snackbarData: SnackbarData) {
	Snackbar(
		snackbarData = snackbarData,
		contentColor = Black,
		shape = RoundedCornerShape(10.dp),
		containerColor = LightYellow
	)
}