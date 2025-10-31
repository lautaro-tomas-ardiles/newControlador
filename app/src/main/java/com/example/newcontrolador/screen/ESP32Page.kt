package com.example.newcontrolador.screen

import android.content.pm.ActivityInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newcontrolador.R
import com.example.newcontrolador.utilitis.ArduinoCode
import com.example.newcontrolador.utilitis.SetOrientation
import com.example.newcontrolador.utilitis.TopBar2

@Composable
fun MainESP32Page(navController: NavController) {
	val scroll = rememberScrollState()
	SetOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, LocalContext.current)

	Scaffold(
		topBar = {
			TopBar2("ESP 32", navController)
		},
		containerColor = MaterialTheme.colorScheme.background
	) { padding ->
		Column(
			Modifier
				.padding(padding)
				.fillMaxSize()
				.padding(horizontal = 10.dp)
				.verticalScroll(scroll),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
			Spacer(Modifier.height(10.dp))

			Image(
				painter = painterResource(id = R.drawable.esp_32),
				contentDescription = "ESP 32 Diagram"
			)
			Spacer(Modifier.height(10.dp))

			Text(
				text = "Codigo de ejemplo para ESP 32",
				color = MaterialTheme.colorScheme.secondary
			)
			Spacer(Modifier.height(10.dp))

			ArduinoCode(
				"""
                    #include <BluetoothSerial.h>

                    //motores
                    int ENA = 12, pinA1 = 25, pinA2 = 33;  //motor A
                    int ENB = 14, pinB1 = 32, pinB2 = 35;  //motor B
                    int velocidad = 70;

                    //bluetooth
                    BluetoothSerial SerialBT;
                    char cmd;

                    void setup() {
                        Serial.begin(115200);
                        SerialBT.begin("Robot A2");

                        pinMode(pinA1, OUTPUT);
                        pinMode(pinA2, OUTPUT);

                        pinMode(pinB1, OUTPUT);
                        pinMode(pinB2, OUTPUT);

                        pinMode(ENA, OUTPUT);
                        pinMode(ENB, OUTPUT);
                    }

                    void loop() {
                    if (SerialBT.available()) {
                        cmd = SerialBT.read();
                    } else {
                        cmd = 'S';  // parar si no hay comando
                    }

                    switch (cmd) {
                        case 'F':
                            adelante();
                            break;
                        case 'B':
                            atras();
                            break;
                        case 'R':
                            derecha();
                            break;
                        case 'L':
                            izquierda();
                            break;
                        case 'G':
                            arribaIzquierda();
                            break;
                        case 'I':
                            arribaDerecha();
                            break;
                        case 'H':
                            abajoIzquierda();
                            break;
                        case 'J':
                            abajoDerecha();
                            break;
                        case 'S':
                            parar();
                            break;
                        default:  // si se recibe un comando no válido
                            parar();
                            break;
                    }
                    }
                    //movimientos del robot
                    // F
                    void adelante() {
                        AmotorAdelante(true);
                        BmotorAdelante(true);
                    }
                    // B
                    void atras() {
                        AmotorAtras(true);
                        BmotorAtras(true);
                    }
                    // R
                    void derecha() {
                        AmotorAdelante(true);
                        BmotorAtras(true);
                    }
                    // L
                    void izquierda() {
                        BmotorAdelante(true);
                        AmotorAtras(true);
                    }
                    // G
                    void arribaIzquierda() {
                        AmotorAdelante(false);
                        BmotorAdelante(true);
                    }
                    // I
                    void arribaDerecha() {
                        BmotorAdelante(false);
                        AmotorAdelante(true);
                    }
                    // H
                    void abajoIzquierda() {
                        AmotorAtras(false);
                        BmotorAtras(true);
                    }
                    // J
                    void abajoDerecha() {
                        AmotorAtras(true);
                        BmotorAtras(false);
                    }
                    // S
                    void parar() {
                        digitalWrite(pinA1, LOW);
                        digitalWrite(pinA2, LOW);
                        digitalWrite(pinB1, LOW);
                        digitalWrite(pinB2, LOW);
                    }

                    //movimiento de los motores
                    void AmotorAdelante(bool velocidadCompleta) {
                        //velo es igual a velocidad si velocidadCompleta es true
                        int velo = velocidadCompleta ? velocidad : velocidad / 3;

                        digitalWrite(pinA1, HIGH);
                        digitalWrite(pinA2, LOW);
                        analogWrite(ENA, velo);
                    }

                    void AmotorAtras(bool velocidadCompleta) {
                        int velo = velocidadCompleta ? velocidad : velocidad / 3;

                        digitalWrite(pinA1, LOW);
                        digitalWrite(pinA2, HIGH);
                        analogWrite(ENA, velo);
                    }

                    void BmotorAdelante(bool velocidadCompleta) {
                        int velo = velocidadCompleta ? velocidad : velocidad / 3;

                        digitalWrite(pinB1, HIGH);
                        digitalWrite(pinB2, LOW);
                        analogWrite(ENB, velo);
                    }

                    void BmotorAtras(bool velocidadCompleta) {
                        int velo = velocidadCompleta ? velocidad : velocidad / 3;

                        digitalWrite(pinB1, LOW);
                        digitalWrite(pinB2, HIGH);
                        analogWrite(ENB, velo);
                    }
                """.trimIndent()
			)
		}
	}
}