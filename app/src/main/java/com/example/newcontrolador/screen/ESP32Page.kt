package com.example.newcontrolador.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.newcontrolador.utilitis.TopBar2

@Composable
fun MainESP32Page(navController: NavController) {
    Scaffold(
        topBar = {
            TopBar2("esp 32", navController)
        }
    ) { padding ->
        Column(
            Modifier.padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

        }
    }
}