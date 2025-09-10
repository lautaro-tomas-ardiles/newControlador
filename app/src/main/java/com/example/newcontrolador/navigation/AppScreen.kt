package com.example.newcontrolador.navigation

sealed class AppScreen(val route: String) {
    object MainPage : AppScreen("main_page")
    object ESP32Page : AppScreen("esp_32_page")
    object ESP8622Page : AppScreen("esp_8622_page")
}