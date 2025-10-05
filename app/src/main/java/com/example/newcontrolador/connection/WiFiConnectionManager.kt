package com.example.newcontrolador.connection

import com.example.newcontrolador.exceptions.*
import java.io.IOException
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.net.UnknownHostException

class WiFiConnectionManager {
	private var espIp: String? = null

	@Throws(Exception::class)
	fun connectToIp(ip: String) {
		// Validar formato básico de IP
		if (!ip.matches(Regex("^\\d{1,3}(\\.\\d{1,3}){3}$"))) {
			throw InvalidIpException("Formato de IP inválido: $ip")
		}

		val url = "http://$ip/"

		try {
			val connection = URL(url).openConnection() as HttpURLConnection
			connection.connectTimeout = 3000
			connection.readTimeout = 3000
			connection.requestMethod = "GET"

			val responseCode = connection.responseCode
			connection.disconnect()

			if (responseCode == 200) {
				espIp = ip
			} else {
				throw UnexpectedResponseException("respondió con código: $responseCode")
			}
		} catch (_: SocketTimeoutException) {
			throw ConnectionTimeoutException("Tiempo de espera agotado al conectar con $ip")
		} catch (_: UnknownHostException) {
			throw DeviceNotFoundException("No se encontró el host: $ip")
		} catch (_: ConnectException) {
			throw ConnectionFailedException("No se pudo conectar al dispositivo")
		} catch (e: IOException) {
			throw ConnectionFailedException("Error de conexión: ${e.message}")
		}
	}

	@Throws(Exception::class)
	fun sendCharWifi(char: Char) {
		val ip = espIp ?: throw InvalidIpException("No hay una IP configurada")

		val url = "http://$ip/$char"

		try {
			val connection = (URL(url).openConnection() as HttpURLConnection).apply {
				requestMethod = "GET"
				connectTimeout = 300
				readTimeout = 300
			}

			val responseCode = connection.responseCode
			connection.disconnect()

			if (responseCode != 200) {
				throw SendCharFailedException("error $responseCode al enviar '$char'")
			}

		} catch (_: SocketTimeoutException) {
			throw ConnectionTimeoutException("Tiempo de espera agotado")
		} catch (_: UnknownHostException) {
			throw DeviceNotFoundException("No se encontró el dispositivo")
		} catch (_: ConnectException) {
			throw ConnectionFailedException("No se pudo conectar")
		} catch (e: IOException) {
			throw SendCharFailedException("Error de conexión: ${e.message}")
		}
	}

}
