@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.newcontrolador.screen

import android.Manifest
import android.bluetooth.*
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.example.newcontrolador.R
import com.example.newcontrolador.connection.BluetoothConnectionManager
import com.example.newcontrolador.ui.theme.*

@Composable
fun BluetoothDevices(
    pairedDevices: Set<BluetoothDevice>, // ✅ Usar Set en vez de MutableSet
    onDeviceClick: (BluetoothDevice) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .background(color = DarkYellow)
            .fillMaxWidth(0.38f)
    ) {
        items(pairedDevices.toList()) { device ->
            DeviceItem(
                device = device,
                onClick = { onDeviceClick(device) } // ✅ Se usa correctamente
            )
        }
    }
}

@Composable
fun DeviceItem(
    device: BluetoothDevice,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }, // ✅ Se asegura que el clic funcione
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Blue)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val context = LocalContext.current
            val deviceName = if (ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.BLUETOOTH_CONNECT
                ) == PackageManager.PERMISSION_GRANTED) {
                device.name ?: "Dispositivo desconocido"
            } else {
                "Permiso requerido"
            }

            Text(text = deviceName, color = Color.White)
            Text(text = device.address, color = Color.Gray)
        }
    }
}
/**
    ambas funciones de arriba solo se llama se si esta usando bluetooth
**/
@Composable
fun TopBar(
    takePermission: ActivityResultLauncher<String>,
    bluetoothAdapter: BluetoothAdapter,
    devicesChange: (Boolean) -> Unit
) {
    var bluetooth by remember { mutableStateOf(true) }
    var ip by remember { mutableStateOf("") }

    val context = LocalContext.current
    val pairedDevices =
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            bluetoothAdapter.bondedDevices
        } else {
            setOf()
        }

    TopAppBar(
        title = {},
        navigationIcon = {
            Switch(
                checked = bluetooth,
                onCheckedChange = { bluetooth = it },
                colors = SwitchDefaults.colors(
                    checkedBorderColor = DarkGreen,
                    checkedTrackColor = LightGreen,
                    checkedThumbColor = DarkGreen,
                    checkedIconColor = Black,

                    uncheckedBorderColor = DarkYellow,
                    uncheckedTrackColor = LightYellow,
                    uncheckedThumbColor = DarkYellow,
                    uncheckedIconColor = Black
                ),
                thumbContent = {
                    if (bluetooth){
                        Icon(
                            painter = painterResource(R.drawable.group_11),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }else {
                        Icon(
                            painter = painterResource(R.drawable.group_10),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            )
        },
        actions = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (bluetooth) {

                    Text(
                        text = "Precione para conectar : ",
                        color = Black,
                        fontSize = 17.sp
                    )
                    IconButton(
                        onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                takePermission.launch(Manifest.permission.BLUETOOTH_CONNECT)
                            }

                            if (pairedDevices.isEmpty()) {
                                Toast.makeText(context, "No se encontraron dispositivos", Toast.LENGTH_SHORT).show()
                            }else {
                                devicesChange(true)
                            }
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = LightYellow
                        )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.group_11),
                            contentDescription = null,
                            tint = Black
                        )
                    }
                } else {
                    OutlinedTextField(
                        value = ip,
                        onValueChange = { ip = it },
                        placeholder = { Text(text = "ingrese la IP") },
                        minLines = 1,
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    if (ip.isNotEmpty()) {

                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = null
                                )
                            }
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = DarkYellow,
                            unfocusedContainerColor = DarkYellow
                        )
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Blue
        )
    )
}

@Composable
fun Button(
    direction: String,
    onPress: (String) -> Unit,
    onRelease: (String) -> Unit
) {
    val arrowDirection = when (direction) {
        "up" -> Icons.Default.KeyboardArrowUp
        "down" -> Icons.Default.KeyboardArrowDown
        "left" -> Icons.AutoMirrored.Filled.KeyboardArrowLeft
        "right" -> Icons.AutoMirrored.Filled.KeyboardArrowRight
        else -> Icons.Default.KeyboardArrowUp
    }

    Box(
        modifier = Modifier
            .size(70.dp)
            .background(
                DarkGreen,
                shape = CircleShape
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        onPress(direction)
                        tryAwaitRelease()
                        onRelease(direction)
                    },
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = arrowDirection,
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            tint = Black
        )
    }
}

@Composable
fun Indicators(pressedButton: String) {
    val colorUp = if ("up" in pressedButton) DarkYellow else Blue
    val colorDown = if ("down" in pressedButton) DarkYellow else Blue
    val colorLeft = if ("left" in pressedButton) DarkYellow else Blue
    val colorRight = if ("right" in pressedButton) DarkYellow else Blue

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
    manager: BluetoothConnectionManager
) {
    var directionsPressed by remember { mutableStateOf(setOf<String>()) }
    val context = LocalContext.current

    Row(
        Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column {
            Button(
                direction = "up",
                onPress = {
                    //lista de teclas presionadas
                    directionsPressed = directionsPressed.toMutableSet().apply { add(it) }

                    //enviar comando al dispositivo por Bluetooth
                    manager.sendChar(
                        when {
                            directionsPressed.contains("up") -> 'F'
                            directionsPressed.contains("down") -> 'B'
                            directionsPressed.contains("left") -> 'L'
                            directionsPressed.contains("right") -> 'R'
                            directionsPressed.contains("up") && directionsPressed.contains("left") -> 'G'
                            directionsPressed.contains("up") && directionsPressed.contains("right") -> 'I'
                            directionsPressed.contains("down") && directionsPressed.contains("left") -> 'H'
                            directionsPressed.contains("down") && directionsPressed.contains("right") -> 'J'

                            else -> ' '
                        },
                        context
                    )
                },
                onRelease = {
                    directionsPressed = directionsPressed.toMutableSet().apply { remove(it) }

                    manager.sendChar('S', context)
                }
            )
            Spacer(Modifier.padding(15.dp))

            Button(
                direction = "down",
                onPress = {
                    //lista de teclas presionadas
                    directionsPressed = directionsPressed.toMutableSet().apply { add(it) }

                    //enviar comando al dispositivo por Bluetooth
                    manager.sendChar(
                        when {
                            directionsPressed.contains("up") -> 'F'
                            directionsPressed.contains("down") -> 'B'
                            directionsPressed.contains("left") -> 'L'
                            directionsPressed.contains("right") -> 'R'
                            directionsPressed.contains("up") && directionsPressed.contains("left") -> 'G'
                            directionsPressed.contains("up") && directionsPressed.contains("right") -> 'I'
                            directionsPressed.contains("down") && directionsPressed.contains("left") -> 'H'
                            directionsPressed.contains("down") && directionsPressed.contains("right") -> 'J'

                            else -> ' '
                        },
                        context
                    )

                },
                onRelease = {
                    directionsPressed = directionsPressed.toMutableSet().apply { remove(it) }

                    manager.sendChar('S', context)

                }
            )
        }

        Indicators(directionsPressed.joinToString(" "))

        Row {
            Button(
                direction = "left",
                onPress = {
                    //lista de teclas presionadas
                    directionsPressed = directionsPressed.toMutableSet().apply { add(it) }

                    //enviar comando al dispositivo por Bluetooth
                    manager.sendChar(
                        when {
                            directionsPressed.contains("up") -> 'F'
                            directionsPressed.contains("down") -> 'B'
                            directionsPressed.contains("left") -> 'L'
                            directionsPressed.contains("right") -> 'R'
                            directionsPressed.contains("up") && directionsPressed.contains("left") -> 'G'
                            directionsPressed.contains("up") && directionsPressed.contains("right") -> 'I'
                            directionsPressed.contains("down") && directionsPressed.contains("left") -> 'H'
                            directionsPressed.contains("down") && directionsPressed.contains("right") -> 'J'

                            else -> ' '
                        },
                        context
                    )

                },
                onRelease = {
                    directionsPressed = directionsPressed.toMutableSet().apply { remove(it) }

                    manager.sendChar('S', context)

                }
            )
            Spacer(Modifier.padding(15.dp))

            Button(
                direction = "right",
                onPress = {
                    //lista de teclas presionadas
                    directionsPressed = directionsPressed.toMutableSet().apply { add(it) }

                    //enviar comando al dispositivo por Bluetooth
                    manager.sendChar(
                        when {
                            directionsPressed.contains("up") -> 'F'
                            directionsPressed.contains("down") -> 'B'
                            directionsPressed.contains("left") -> 'L'
                            directionsPressed.contains("right") -> 'R'
                            directionsPressed.contains("up") && directionsPressed.contains("left") -> 'G'
                            directionsPressed.contains("up") && directionsPressed.contains("right") -> 'I'
                            directionsPressed.contains("down") && directionsPressed.contains("left") -> 'H'
                            directionsPressed.contains("down") && directionsPressed.contains("right") -> 'J'

                            else -> ' '
                        },
                        context
                    )
                },
                onRelease = {
                    directionsPressed = directionsPressed.toMutableSet().apply { remove(it) }

                    manager.sendChar('S', context)

                }
            )
        }
    }
}

@Composable
fun MainScreen(
    takePermission: ActivityResultLauncher<String>,
    bluetoothAdapter: BluetoothAdapter
) {
    var devices by remember { mutableStateOf(false) }

    val bluetoothConnectionManager = remember { BluetoothConnectionManager() }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopBar(
                takePermission,
                bluetoothAdapter,
                devicesChange = { devices = !devices }
            )
        },
        containerColor = Black
    ) { padding ->
        Column(
            Modifier.padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val onDeviceClick: (BluetoothDevice) -> Unit = { device ->
                try {
                    if (
                        ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {

                        if (
                            bluetoothConnectionManager.connectToDevice(device, context)
                        ) {
                            Toast.makeText(context, "Conectado a ${device.name}", Toast.LENGTH_SHORT).show()
                            devices = false // ✅ Cierra la lista después de la conexión
                        } else {
                            Toast.makeText(context, "No se pudo conectar a ${device.name}", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Permiso BLUETOOTH_CONNECT no concedido", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "${e.message}", Toast.LENGTH_LONG).show()
                }
            }

            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                GridButton(
                    manager = bluetoothConnectionManager
                )

                if (devices) {
                    BluetoothDevices(
                        pairedDevices = bluetoothAdapter.bondedDevices,
                        onDeviceClick = onDeviceClick  // ✅ Ya no da "The expression is unused"
                    )
                }
            }
        }
    }
}