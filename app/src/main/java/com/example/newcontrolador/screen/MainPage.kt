package com.example.newcontrolador.screen

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.example.newcontrolador.connection.BluetoothConnectionManager
import com.example.newcontrolador.connection.Directions
import com.example.newcontrolador.connection.Modes
import com.example.newcontrolador.connection.WiFiConnectionManager
import com.example.newcontrolador.ui.theme.Black
import com.example.newcontrolador.ui.theme.Blue
import com.example.newcontrolador.ui.theme.DarkYellow
import com.example.newcontrolador.utilitis.BluetoothDevices
import com.example.newcontrolador.utilitis.Button
import com.example.newcontrolador.utilitis.TopBarForMainPage
import com.example.newcontrolador.utilitis.getDirectionChar

@Composable
fun Indicators(pressedButton: Set<Directions>) {
	val colorUp = if (Directions.UP in pressedButton) DarkYellow else Blue
	val colorDown = if (Directions.DOWN in pressedButton) DarkYellow else Blue
	val colorLeft = if (Directions.LEFT in pressedButton) DarkYellow else Blue
	val colorRight = if (Directions.RIGHT in pressedButton) DarkYellow else Blue

	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.Center
	) {
		Icon(
			imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
			contentDescription = null,
			modifier = Modifier.size(65.dp),
			tint = colorLeft
		)
		Column {
			Icon(
				imageVector = Icons.Default.KeyboardArrowUp,
				contentDescription = null,
				modifier = Modifier.size(65.dp),
				tint = colorUp
			)
			Spacer(Modifier.padding(25.dp))

			Icon(
				imageVector = Icons.Default.KeyboardArrowDown,
				contentDescription = null,
				modifier = Modifier.size(65.dp),
				tint = colorDown
			)
		}

		Icon(
			imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
			contentDescription = null,
			modifier = Modifier.size(65.dp),
			tint = colorRight
		)
	}
}

@Composable
fun GridButton(
	managerBluetooth: BluetoothConnectionManager,
	managerWiFi: WiFiConnectionManager,
	isBluetooth: Boolean,
) {
	var directionsPressed by remember { mutableStateOf(setOf<Directions>()) }
	val context = LocalContext.current

	fun sendChar(char: Char) {
		if (isBluetooth) {
			managerBluetooth.sendChar(char, context)
		} else {
			managerWiFi.sendChar(char, context)
		}
	}

	Row(
		Modifier.fillMaxSize(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Column {
			Button(
				direction = Directions.UP,
				onPress = {
					directionsPressed = directionsPressed.toMutableSet().apply { add(it) }

					sendChar(getDirectionChar(directionsPressed))
				},
				onRelease = {
					directionsPressed = directionsPressed.toMutableSet().apply { remove(it) }

					sendChar(Directions.STOP.char)
				}
			)

			Button(
				direction = Directions.DOWN,
				onPress = {
					directionsPressed = directionsPressed.toMutableSet().apply { add(it) }

					sendChar(getDirectionChar(directionsPressed))
				},
				onRelease = {
					directionsPressed = directionsPressed.toMutableSet().apply { remove(it) }

					sendChar(Directions.STOP.char)
				}
			)
		}

		Indicators(directionsPressed)

		Row {
			Button(
				direction = Directions.LEFT,
				onPress = {
					directionsPressed = directionsPressed.toMutableSet().apply { add(it) }

					sendChar(getDirectionChar(directionsPressed))
				},
				onRelease = {
					directionsPressed = directionsPressed.toMutableSet().apply { remove(it) }

					sendChar(Directions.STOP.char)
				}
			)

			Button(
				direction = Directions.RIGHT,
				onPress = {
					directionsPressed = directionsPressed.toMutableSet().apply { add(it) }

					sendChar(getDirectionChar(directionsPressed))
				},
				onRelease = {
					directionsPressed = directionsPressed.toMutableSet().apply { remove(it) }

					sendChar(Directions.STOP.char)
				}
			)
		}
	}
}

@Composable
fun MainScreen(
	bluetoothAdapter: BluetoothAdapter,
	navController: NavController
) {

	var devices by remember { mutableStateOf(false) }
	var bluetooth by remember { mutableStateOf(false) }

	var modeSelected by remember { mutableStateOf(Modes.MANUAL) }

	val bluetoothConnectionManager = remember { BluetoothConnectionManager() }
	val wifiManager = remember { WiFiConnectionManager() }

	val context = LocalContext.current

	LaunchedEffect(modeSelected) {
		bluetoothConnectionManager.sendChar(modeSelected.char, context)
	}

	Scaffold(
		topBar = {
			TopBarForMainPage(
				bluetoothAdapter = bluetoothAdapter,
				wiFiConnectionManager = wifiManager,
				navController = navController,
				isBluetoothEnable = { bluetooth = it },
				devicesChange = { devices = it },
				modeSelected = { modeSelected = it }
			)
		},
		containerColor = Black
	) { padding ->
		Box(
			Modifier
                .padding(padding)
                .fillMaxSize(),
			contentAlignment = Alignment.Center
		) {
			GridButton(
				managerBluetooth = bluetoothConnectionManager,
				managerWiFi = wifiManager,
				isBluetooth = bluetooth
			)

			val hasPermission =
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
					ActivityCompat.checkSelfPermission(
						context,
						Manifest.permission.BLUETOOTH_CONNECT
					) == PackageManager.PERMISSION_GRANTED
				} else {
					false
				}

			if (devices && hasPermission) {
				BluetoothDevices(
					pairedDevices = bluetoothAdapter.bondedDevices
				) {
					try {
						val connectBluetooth =
							bluetoothConnectionManager.connectToDevice(it, context)

						if (connectBluetooth) {
							Toast.makeText(
								context,
								"Conectado a ${it.name}",
								Toast.LENGTH_SHORT
							).show()
							bluetoothConnectionManager.listenForAllDevices(context)
							devices = false
						} else {
							Toast.makeText(
								context,
								"No se pudo conectar a ${it.name}",
								Toast.LENGTH_SHORT
							).show()
							devices = false
						}
					} catch (e: Exception) {
						e.printStackTrace()
						Toast.makeText(context, "${e.message}", Toast.LENGTH_LONG).show()
					}
				}
			}

		}
	}
}

/*
@Composable
fun boton(direction: Directions) {
    val arrowDirection = when (direction) {
        Directions.UP -> Icons.Default.KeyboardArrowUp
        Directions.DOWN -> Icons.Default.KeyboardArrowDown
        Directions.LEFT -> Icons.AutoMirrored.Filled.KeyboardArrowLeft
        Directions.RIGHT -> Icons.AutoMirrored.Filled.KeyboardArrowRight
        else -> Icons.Default.KeyboardArrowUp
    }

    Box(
        modifier = Modifier
            //.heightIn(80.dp, 150.dp) se pone el minimo no se como usarlo
            //.widthIn(90.dp,160.dp)
            .height(150.dp)
            .width(165.dp)
            .background(DarkGreen)
            .border(2.dp, LightYellow),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = arrowDirection,
            contentDescription = null,
            modifier = Modifier
                .size(70.dp)
                .background(LightYellow, CircleShape),
            tint = Black
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    device = "spec:width=411dp,height=891dp,orientation=landscape", showSystemUi = true,
    showBackground = true
)
@Composable
private fun s() {
    NewControladorTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "d",
                            color = LightYellow
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Blue
                    )
                )
            },
            containerColor = Black
        ) { paddingValues ->
            Row(
                Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    boton(Directions.UP)

                    boton(Directions.DOWN)
                }
                Indicators(pressedButton = setOf(Directions.UP, Directions.LEFT))

                Row {
                    boton(Directions.LEFT)

                    boton(Directions.RIGHT)
                }
            }
        }
    }
}

@Preview
@Composable
private fun A() {
    var ip by remember { mutableStateOf("") }
    var ip2 by remember { mutableStateOf("") }

    NewControladorTheme {
        Column {
            OutlinedTextField(
                value = ip,
                onValueChange = { ip = it },
                label = {
                    Text("ingrese la IP")
                },
                trailingIcon = {
                    IconsButtons(
                        onClick = {
                        },
                        tintColor = Black,
                        imageVector = Icons.AutoMirrored.Filled.Send
                    )
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = DarkYellow,
                    unfocusedContainerColor = DarkYellow,
                    focusedPlaceholderColor = Black,
                    unfocusedPlaceholderColor = Black,
                    focusedTrailingIconColor = Black,
                    unfocusedTrailingIconColor = Black,
                    focusedTextColor = Black,
                    unfocusedTextColor = Black
                ),
                modifier = Modifier.wrapContentSize()
            )
            Spacer(Modifier.padding(20.dp))
            TextField(
                value = ip2,
                onValueChange = { ip2 = it },
                label = {
                    Text("ingrese la IP")
                },
                trailingIcon = {
                    IconsButtons(
                        onClick = {
                        },
                        tintColor = Black,
                        imageVector = Icons.AutoMirrored.Filled.Send
                    )
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = DarkYellow,
                    unfocusedContainerColor = DarkYellow,
                    focusedTrailingIconColor = Black,
                    unfocusedTrailingIconColor = Black,
                    focusedTextColor = Black,
                    unfocusedTextColor = Black
                ),
                modifier = Modifier.wrapContentSize()
            )
        }
    }
}

 */