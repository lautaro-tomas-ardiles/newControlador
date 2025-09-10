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
import com.example.newcontrolador.utilitis.TopBar
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
        horizontalArrangement = Arrangement.SpaceEvenly
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
            Spacer(Modifier.padding(25.dp))

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
            Spacer(Modifier.padding(25.dp))

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
            TopBar(
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
        Column(
            Modifier.padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                Modifier.fillMaxSize(),
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
                                Toast.makeText(context, "Conectado a ${it.name}", Toast.LENGTH_SHORT).show()
                                bluetoothConnectionManager.listenForAllDevices(context)
                                devices = false
                            } else {
                                Toast.makeText(context, "No se pudo conectar a ${it.name}", Toast.LENGTH_SHORT).show()
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
}