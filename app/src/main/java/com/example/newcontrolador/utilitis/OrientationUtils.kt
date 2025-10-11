package com.example.newcontrolador.utilitis

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable

/**
 * Cambia la orientación de la pantalla de la actividad actual.
 *
 * Esta función convierte el [Context] recibido en una [Activity] y ajusta
 * la orientación solicitada mediante `requestedOrientation`.
 *
 * @param orientation Nueva orientación de la pantalla. Por ejemplo:
 *   [android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT] o
 *   [android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE].
 * @param context Contexto de la aplicación o actividad.
 */
@Composable
fun SetOrientation(orientation: Int, context: Context) {
	val activity = context as? Activity
	activity?.requestedOrientation = orientation
}