package com.example.newcontrolador.utilitis

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable

@Composable
fun SetOrientation(orientation: Int, context: Context) {
	val activity = context as? Activity
	activity?.requestedOrientation = orientation
}