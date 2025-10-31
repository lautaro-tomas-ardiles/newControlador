package com.example.newcontrolador.utilitis

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RestAndLoad(context: Context) {
	rememberCoroutineScope()

	Column {
		Spacer(Modifier.height(5.dp))

		Row(
			verticalAlignment = Alignment.CenterVertically
		) {
			SimpleButton("guardar") {

			}
			Spacer(Modifier.width(10.dp))

			SimpleButton("resetear") {

			}
		}
		Spacer(Modifier.height(5.dp))
	}
}