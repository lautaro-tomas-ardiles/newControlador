@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.newcontrolador.utilitis

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.example.newcontrolador.R
import com.example.newcontrolador.connection.WiFiConnectionManager
import com.example.newcontrolador.ui.theme.Black
import com.example.newcontrolador.ui.theme.Blue
import com.example.newcontrolador.ui.theme.DarkGreen
import com.example.newcontrolador.ui.theme.DarkYellow
import com.example.newcontrolador.ui.theme.LightGreen
import com.example.newcontrolador.ui.theme.LightYellow

fun getDirectionChar(directionsPressed: Set<String>): Char {
    return when {
        directionsPressed.contains("up") && directionsPressed.contains("left") -> 'G'
        directionsPressed.contains("up") && directionsPressed.contains("right") -> 'I'
        directionsPressed.contains("down") && directionsPressed.contains("left") -> 'H'
        directionsPressed.contains("down") && directionsPressed.contains("right") -> 'J'
        directionsPressed.contains("up") -> 'F'
        directionsPressed.contains("down") -> 'B'
        directionsPressed.contains("left") -> 'L'
        directionsPressed.contains("right") -> 'R'
        else -> ' '
    }
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
            .size(90.dp)
            .background(
                color = DarkGreen,
                shape = RoundedCornerShape(35)
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
            modifier = Modifier.size(60.dp),
            tint = Black
        )
    }
}

@Composable
fun BluetoothDevices(
    pairedDevices: Set<BluetoothDevice>,
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
                onClick = { onDeviceClick(device) } // Se usa correctamente
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
            .clickable { onClick() }, // Se asegura que el clic funcione
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Blue)
    ) {
        Column(
            Modifier.padding(16.dp)
        ) {
            val context = LocalContext.current
            val deviceName =
                if (ActivityCompat.checkSelfPermission(
                         context,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    device.name ?: "Dispositivo desconocido"
                } else {
                    "Permiso requerido"
                }

            Text(text = deviceName, color = Color.White)
            Text(text = device.address, color = Color.Gray)
        }
    }
}

@Composable
fun BluetoothSwitch(
    bluetooth: Boolean,
    onBluetoothChange: (Boolean) -> Unit
) {
    Switch(
        checked = bluetooth,
        onCheckedChange = { onBluetoothChange },
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
            if (bluetooth) {
                Icon(
                    painter = painterResource(R.drawable.group_11),
                    contentDescription = "bluetooth icon",
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.group_10),
                    contentDescription = "wifi icon",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    )
}

@Composable
fun WifiTextField(
    wifiManager: WiFiConnectionManager,
    ip: String,
    onIpChange: (String) -> Unit
) {
    val context = LocalContext.current
    OutlinedTextField(
        value = ip,
        onValueChange = { onIpChange(it) },
        placeholder = {
            Text("ingrese la IP")
        },
        minLines = 1,
        trailingIcon = {
            IconButton(
                onClick = {
                    val connectIp = wifiManager.connectToIp(ip, context)
                    if (connectIp) {
                        Toast.makeText(context, "Conectado a $ip", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "No se pudo conectar a $ip", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "boton de enviar"
                )
            }
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
            unfocusedTextColor = Black,
        )
    )
}

@Composable
fun TopBar(
    wifiManager: WiFiConnectionManager,
    bluetoothAdapter: BluetoothAdapter,
    devicesChange: (Boolean) -> Unit,
    isBluetoothEnable: (Boolean) -> Unit
) {
    var bluetooth by remember { mutableStateOf(true) }
    var ip by remember { mutableStateOf("") }

    val context = LocalContext.current
    val pairedDevices =
        if (
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            bluetoothAdapter.bondedDevices
        } else {
            setOf()
        }

    TopAppBar(
        title = {/* No title needed */ },
        navigationIcon = {
            BluetoothSwitch(bluetooth) { b ->
                bluetooth = b
                isBluetoothEnable(b)
            }
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
                            if (pairedDevices.isEmpty()) {
                                Toast.makeText(context, "No se encontraron dispositivos", Toast.LENGTH_SHORT).show()
                            } else {
                                devicesChange(true)
                            }
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = LightYellow
                        )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.group_11),
                            contentDescription = "bluetooth icon",
                            tint = Black
                        )
                    }
                } else {
                    WifiTextField(
                        wifiManager = wifiManager,
                        ip = ip,
                    ) { newIp -> ip = newIp }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Blue
        )
    )
}