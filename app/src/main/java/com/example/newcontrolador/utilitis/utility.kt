@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.newcontrolador.utilitis

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.example.newcontrolador.R
import com.example.newcontrolador.connection.Directions
import com.example.newcontrolador.connection.Modes
import com.example.newcontrolador.connection.WiFiConnectionManager
import com.example.newcontrolador.navigation.AppScreen
import com.example.newcontrolador.ui.theme.Black
import com.example.newcontrolador.ui.theme.Blue
import com.example.newcontrolador.ui.theme.DarkGreen
import com.example.newcontrolador.ui.theme.DarkYellow
import com.example.newcontrolador.ui.theme.LightGreen
import com.example.newcontrolador.ui.theme.LightYellow

fun getDirectionChar(directionsPressed: Set<Directions>): Char {
    return Directions.fromSet(directionsPressed).char
}

@Composable
fun SettingsItem(direction: Directions) {
    val text = when (direction) {
        Directions.UP -> "Arriba"
        Directions.DOWN -> "Abajo"
        Directions.LEFT -> "Izquierda"
        Directions.RIGHT -> "Derecha"
        Directions.UP_LEFT -> "Arriba Izquierda"
        Directions.UP_RIGHT -> "Arriba Derecha"
        Directions.DOWN_LEFT -> "Abajo Izquierda"
        Directions.DOWN_RIGHT -> "Abajo Derecha"
        Directions.STOP -> "Detener"
    }
    var newDirection by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    isError = newDirection.length > 1

    Card(
        modifier = Modifier
            .wrapContentWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp),
        shape = RoundedCornerShape(10),
        colors = CardDefaults.cardColors(containerColor = LightGreen),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        border = BorderStroke(2.dp, LightYellow)
    ) {
        Spacer(Modifier.padding(5.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.padding(5.dp))

            Text(
                text = text,
                color = Black
            )
            Spacer(Modifier.padding(10.dp))

            OutlinedTextField(
                value = newDirection,
                onValueChange = {
                    newDirection = it
                    val newChar = it.toCharArray()
                    when (direction) {
                        Directions.UP -> Directions.UP.char = newChar[0]
                        Directions.DOWN -> Directions.DOWN.char = newChar[0]
                        Directions.LEFT -> Directions.LEFT.char = newChar[0]
                        Directions.RIGHT -> Directions.RIGHT.char = newChar[0]
                        Directions.UP_LEFT -> Directions.UP_LEFT.char = newChar[0]
                        Directions.UP_RIGHT -> Directions.UP_RIGHT.char = newChar[0]
                        Directions.DOWN_LEFT -> Directions.DOWN_LEFT.char = newChar[0]
                        Directions.DOWN_RIGHT -> Directions.DOWN_RIGHT.char = newChar[0]
                        Directions.STOP -> Directions.STOP.char = newChar[0]
                    }
                },
                placeholder = {
                    Text(
                        text = "${direction.char}",
                        color = Blue
                    )
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = LightYellow,
                    focusedBorderColor = DarkYellow,
                    focusedTextColor = Black,
                    unfocusedLabelColor = Black
                ),
                modifier = Modifier.width(50.dp),
                isError = isError
            )
            Spacer(Modifier.padding(5.dp))
        }
        Spacer(Modifier.padding(5.dp))
    }
}

@Composable
fun Button(
    direction: Directions,
    onPress: (Directions) -> Unit,
    onRelease: (Directions) -> Unit
) {
    val arrowDirection = when (direction) {
        Directions.UP -> Icons.Default.KeyboardArrowUp
        Directions.DOWN -> Icons.Default.KeyboardArrowDown
        Directions.LEFT -> Icons.AutoMirrored.Filled.KeyboardArrowLeft
        Directions.RIGHT -> Icons.AutoMirrored.Filled.KeyboardArrowRight
        else -> Icons.Default.KeyboardArrowUp
    }

    Box(
        modifier = Modifier
            .size(100.dp)
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
            modifier = Modifier.size(70.dp),
            tint = Black
        )
    }
}

@Composable
fun IconsButtons(
    onClick: () -> Unit,
    isSolidColor: Boolean = false,
    isBluetooth: Boolean = false,
    border: Boolean = false,
    tintColor: Color = LightGreen,
    imageVector: ImageVector = Icons.Default.Settings
) {
    IconButton(
        onClick = { onClick() },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = if (isSolidColor) LightYellow else Color.Transparent,
        ),
        modifier = Modifier
            .size(45.dp)
            .border(
                width = 3.dp,
                color = if (border) LightYellow else Color.Transparent,
                shape = CircleShape
            )
    ) {
        if (isBluetooth) {
            Icon(
                painter = painterResource(R.drawable.group_11),
                contentDescription = null,
                tint = Black
            )
        } else {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                tint = tintColor,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
fun TextAndButton(
    text: String,
    imageVector: ImageVector = Icons.Default.MoreVert,
    isBluetooth: Boolean = false,
    onClick: () -> Unit
) {
    Text(
        text = text,
        color = Black,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize
    )
    Spacer(modifier = Modifier.padding(3.dp))

    IconsButtons(
        onClick = { onClick() },
        border = !isBluetooth, // si es bluetooth no debe tener borde
        imageVector = imageVector,
        isBluetooth = isBluetooth,
        isSolidColor = isBluetooth // si es el boton de bluetooth tiene que ser solido
    )
}

@Composable
fun ModeIcon(
    text: String,
    onClick: () -> Unit,
    stateOfItem: Boolean
) {
    DropdownMenuItem(
        text = {
            Text(text = text, color = Black)
        },
        onClick = { onClick() },
        trailingIcon = {
            RadioButton(
                selected = stateOfItem,
                onClick = { onClick() },
                colors = RadioButtonDefaults.colors(
                    selectedColor = Blue,
                    unselectedColor = Blue
                )
            )
        },
        modifier = Modifier
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val y = size.height - strokeWidth / 2
                drawLine(
                    color = Blue,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeWidth
                )
            }
    )
}

@Composable
fun BluetoothDevices(
    pairedDevices: Set<BluetoothDevice>,
    onDeviceClick: (BluetoothDevice) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .background(color = DarkYellow)
            .fillMaxWidth(0.4f)
    ) {
        items(
            items = pairedDevices.toList()
        ) { device ->
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

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Blue)
    ) {
        Column(
            Modifier.padding(16.dp)
        ) {
            Text(
                text = deviceName,
                color = Color.White
            )
            Text(
                text = device.address,
                color = Color.Gray
            )
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
        onCheckedChange = { onBluetoothChange(it) },
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
            IconsButtons(
                onClick = {
                    val connectIp = wifiManager.connectToIp(ip, context)
                    if (connectIp) {
                        Toast.makeText(context, "Conectado a $ip", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "No se pudo conectar a $ip", Toast.LENGTH_SHORT).show()
                    }
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
        )
    )
}

//soy vago se queda asi
@Composable
fun TopBar2(text: String, navController: NavController) {
    TopAppBar(
        title = {
            Text(
                text = text,
                color = LightYellow
            )
        },
        navigationIcon = {
            Row {
                Spacer(Modifier.padding(start = 30.dp))

                IconsButtons(
                    onClick = {
                        navController.navigate(AppScreen.MainPage.route)
                    },
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Blue
        )
    )
}

@Composable
fun TopBar(
    bluetoothAdapter: BluetoothAdapter,
    wiFiConnectionManager: WiFiConnectionManager,
    navController: NavController,
    isBluetoothEnable: (Boolean) -> Unit,
    devicesChange: (Boolean) -> Unit,
    modeSelected: (Modes) -> Unit
) {
    var bluetooth by remember { mutableStateOf(true) }
    var ip by remember { mutableStateOf("") }

    var menuModeState by remember { mutableStateOf(false) }
    var modeSelect by remember { mutableStateOf(Modes.MANUAL) }

    var menuDiagramasState by remember { mutableStateOf(false) }

    var menuSettingState by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val pairedDevices: Set<BluetoothDevice>
    if (
        ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.BLUETOOTH_CONNECT
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        pairedDevices = bluetoothAdapter.bondedDevices
    } else {
        pairedDevices = setOf()
    }

    val allDirections = remember {
        listOf(
            Directions.UP,
            Directions.DOWN,
            Directions.LEFT,
            Directions.RIGHT,
            Directions.UP_LEFT,
            Directions.UP_RIGHT,
            Directions.DOWN_LEFT,
            Directions.DOWN_RIGHT,
            Directions.STOP
        )
    }


    TopAppBar(
        title = { /*TODO: no se nesesita*/ },
        navigationIcon = {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 30.dp)
            ) {
                BluetoothSwitch(bluetooth) {
                    bluetooth = it
                    isBluetoothEnable(it)
                }
                Spacer(Modifier.width(20.dp))

                if (bluetooth) {
                    TextAndButton(
                        text = "Precione para conectar :",
                        isBluetooth = true
                    ) {
                        if (pairedDevices.isEmpty()) {
                            Toast.makeText(context, "No se encontraron dispositivos", Toast.LENGTH_SHORT).show()
                        } else {
                            devicesChange(true)
                        }
                    }
                } else {
                    WifiTextField(
                        wifiManager = wiFiConnectionManager,
                        ip = ip
                    ) { newIp ->
                        ip = newIp
                    }
                }
            }
        },
        actions = {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 30.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Row (verticalAlignment = Alignment.CenterVertically ) {
                    TextAndButton(
                        text = "modo del robot"
                    ) {
                        menuModeState = !menuModeState
                    }
                    DropdownMenu(
                        expanded = menuModeState,
                        onDismissRequest = { menuModeState = false },
                        modifier = Modifier
                            .background(color = LightGreen)
                            .wrapContentSize()
                    ) {
                        ModeIcon(
                            text = "Automata",
                            onClick = {
                                modeSelect = Modes.AUTOMATA
                                menuModeState = false
                                modeSelected(modeSelect)
                            },
                            stateOfItem = modeSelect == Modes.AUTOMATA
                        )
                        ModeIcon(
                            text = "control manual",
                            onClick = {
                                modeSelect = Modes.MANUAL
                                menuModeState = false
                                modeSelected(modeSelect)
                            },
                            stateOfItem = modeSelect == Modes.MANUAL
                        )
                    }
                }
                Spacer(Modifier.width(10.dp))

                Row (verticalAlignment = Alignment.CenterVertically ) {
                    TextAndButton("diagramas de robots") {
                        menuDiagramasState = !menuDiagramasState
                    }
                    DropdownMenu(
                        expanded = menuDiagramasState,
                        onDismissRequest = { menuDiagramasState = false },
                        modifier = Modifier
                            .background(color = LightGreen)
                            .wrapContentSize()
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(text = "ESP 32", color = Black)
                            },
                            onClick = { navController.navigate(AppScreen.ESP32Page.route) }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(text = "ESP 8622", color = Black)
                            },
                            onClick = { navController.navigate(AppScreen.ESP8622Page.route) }
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(10.dp))

                Row (verticalAlignment = Alignment.CenterVertically ) {
                    IconsButtons(
                        onClick = { menuSettingState = !menuSettingState },
                        isSolidColor = false,
                    )
                    DropdownMenu(
                        expanded = menuSettingState,
                        onDismissRequest = { menuSettingState = false },
                        modifier = Modifier
                            .background(LightGreen)
                            .wrapContentSize()
                            .heightIn(max = 300.dp)
                    ) {
                        allDirections.forEach { it ->
                            SettingsItem(it)
                        }
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Blue
        )
    )
}
