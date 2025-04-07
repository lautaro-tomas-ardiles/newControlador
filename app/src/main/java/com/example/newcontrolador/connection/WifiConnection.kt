package com.example.newcontrolador.connection

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class WiFiConnection(private val ipAddress: String) {

    private val client = OkHttpClient()

    /**
     * Verifica si el ESP8266 es accesible mediante una solicitud HTTP GET.
     * @return `true` si la conexión es exitosa, `false` en caso contrario.
     */
    fun isReachable(): Boolean {
        val url = "http://$ipAddress/ping" // Ruta específica para verificar la conexión
        val request = Request.Builder()
            .url(url)
            .build()

        return try {
            client.newCall(request).execute().use { response ->
                response.isSuccessful
            }
        } catch (e: IOException) {
            false // Ocurrió un error al intentar conectar
        }
    }

    /**
     * Envía un carácter al ESP8266 a través de una solicitud HTTP GET.
     * @param charToSend Carácter que se enviará al dispositivo.
     * @return Respuesta del servidor como String, o null en caso de error.
     */
    fun sendChar(charToSend: Char): String? {
        val url = "http://$ipAddress/send?char=$charToSend"
        val request = Request.Builder()
            .url(url)
            .build()

        return try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    println("Error en la solicitud: ${response.code}")
                    null
                } else {
                    response.body?.string()
                }
            }
        } catch (e: IOException) {
            println("Excepción al enviar la solicitud: ${e.message}")
            null
        }
    }
}