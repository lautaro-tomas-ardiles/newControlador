package com.example.newcontrolador.connection

import com.example.newcontrolador.exceptions.*
import java.io.IOException
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.net.UnknownHostException

/**
 * Gestor de conexión WiFi para comunicarse con un dispositivo mediante HTTP.
 *
 * Permite conectarse a un IP específica y enviar caracteres como comandos.
 */
class WiFiConnectionManager {

	//* IP del dispositivo al que estamos conectados. *
	private var espIp: String? = null

	/**
	 * Intenta conectar a un dispositivo mediante su IP.
	 *
	 * @param ip IP del dispositivo (formato "192.168.x.x").
	 * @throws InvalidIpException Si el formato de la IP es incorrecto.
	 * @throws ConnectionTimeoutException Si la conexión tarda demasiado.
	 * @throws DeviceNotFoundException Si no se encuentra el host.
	 * @throws ConnectionFailedException Si ocurre cualquier otro fallo de conexión.
	 * @throws UnexpectedResponseException Si la respuesta HTTP no es 200 OK.
	 */
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
				throw UnexpectedResponseException("Respondió con código: $responseCode")
			}
		} catch (_: SocketTimeoutException) {
			throw ConnectionTimeoutException("Tiempo de espera agotado al conectar con $ip")
		} catch (_: UnknownHostException) {
			throw DeviceNotFoundException("No se encontró el host: $ip")
		} catch (_: ConnectException) {
			throw ConnectionFailedException("No se pudo conectar al dispositivo $ip")
		} catch (e: IOException) {
			throw ConnectionFailedException("Error de conexión: ${e.message}")
		}
	}

	/**
	 * Envía un carácter al dispositivo conectado mediante HTTP.
	 *
	 * @param char Carácter a enviar como comando.
	 * @throws InvalidIpException Si no hay una IP configurada.
	 * @throws ConnectionTimeoutException Si la conexión tarda demasiado.
	 * @throws DeviceNotFoundException Si no se encuentra el dispositivo.
	 * @throws ConnectionFailedException Si ocurre cualquier otro fallo de conexión.
	 * @throws SendCharFailedException Si la respuesta HTTP no es 200 OK.
	 */
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
				throw SendCharFailedException("Error $responseCode al enviar '$char'")
			}

		} catch (_: SocketTimeoutException) {
			throw ConnectionTimeoutException("Tiempo de espera agotado al enviar '$char'")
		} catch (_: UnknownHostException) {
			throw DeviceNotFoundException("No se encontró el dispositivo")
		} catch (_: ConnectException) {
			throw ConnectionFailedException("No se pudo conectar al dispositivo")
		} catch (e: IOException) {
			throw SendCharFailedException("Error de conexión: ${e.message}")
		}
	}
}