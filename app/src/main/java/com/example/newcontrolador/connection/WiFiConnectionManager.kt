package com.example.newcontrolador.connection

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class WiFiConnectionManager {
	private var espIp: String? = null

	fun connectToIp(ip: String, context: Context): Boolean {
		// Validar formato básico de IP
		if (!ip.matches(Regex("^\\d{1,3}(\\.\\d{1,3}){3}$"))) {
			return false
		}

		val url = "http://$ip/" // Puedes cambiar "/" por "/ping"

		return try {
			val connection = URL(url).openConnection() as HttpURLConnection
			connection.connectTimeout = 3000
			connection.readTimeout = 3000
			connection.requestMethod = "GET"

			val responseCode = connection.responseCode
			connection.disconnect()

			if (responseCode == 200) {
				espIp = ip
				true
			} else {
				Handler(Looper.getMainLooper()).post {
					Toast.makeText(
						context,
						"La ESP8266 no respondió (código: $responseCode)",
						Toast.LENGTH_SHORT
					).show()
				}
				false
			}
		} catch (e: IOException) {
			e.printStackTrace()
			false
		}
	}

	fun sendChar(char: Char, context: Context) {
		if (espIp == null) {
			return
		}

		val url = "http://$espIp/${char}"

		Thread {
			try {
				val connection = URL(url).openConnection() as HttpURLConnection
				connection.requestMethod = "GET"
				connection.connectTimeout = 300
				connection.readTimeout = 300
				val responseCode = connection.responseCode
				connection.disconnect()

				if (responseCode != 200) {
					Handler(Looper.getMainLooper()).post {
						Toast.makeText(
							context,
							"Error al enviar: error $responseCode",
							Toast.LENGTH_SHORT
						).show()
					}
				}
			} catch (e: IOException) {
				e.printStackTrace()
			}
		}.start()
	}
}
