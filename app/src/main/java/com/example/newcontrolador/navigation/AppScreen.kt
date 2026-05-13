package com.example.newcontrolador.navigation

sealed class AppScreen(val route: String) {
	object MainPage : AppScreen("main_page")
	object SocialPage : AppScreen("social_page")
}