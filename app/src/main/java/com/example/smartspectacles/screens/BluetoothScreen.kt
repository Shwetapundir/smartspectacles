package com.example.smartspectacles.screens


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.smartspectacles.viewmodel.DevicesViewModel

@Composable
fun BluetoothScannerScreen(navController: NavController,bluetoothViewModel: DevicesViewModel) {
    val context = LocalContext.current
    val scannedDevices by bluetoothViewModel.scannedDevices.collectAsState()

    // ⬇️ Heading calculation starts here
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val headingState = remember { mutableStateOf(0f) }

    DisposableEffect(Unit) {
        val rotationMatrix = FloatArray(9)
        val orientation = FloatArray(3)
        val gravity = FloatArray(3)
        val geomagnetic = FloatArray(3)

        val sensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                when (event.sensor.type) {
                    Sensor.TYPE_ACCELEROMETER -> {
                        System.arraycopy(event.values, 0, gravity, 0, 3)
                    }
                    Sensor.TYPE_MAGNETIC_FIELD -> {
                        System.arraycopy(event.values, 0, geomagnetic, 0, 3)
                        if (SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic)) {
                            SensorManager.getOrientation(rotationMatrix, orientation)
                            val azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                            headingState.value = (azimuth + 360) % 360
                        }
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        val accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magnetSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        sensorManager.registerListener(sensorListener, accelSensor, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(sensorListener, magnetSensor, SensorManager.SENSOR_DELAY_UI)

        onDispose {
            sensorManager.unregisterListener(sensorListener)
        }
    }

    val heading = headingState.value

    // UI Starts
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Bluetooth Scanner", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(onClick = { bluetoothViewModel.startScan(context) }) {
                Text("Start Scan")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { bluetoothViewModel.stopScan(context) }) {
                Text("Stop Scan")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (scannedDevices.isEmpty()) {
            Text(text = "No devices found", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn {
                items(scannedDevices) { device ->
//                    DiscoveredDeviceItem(device, heading) {
//                        bluetoothViewModel.connectToDevice(device.device)
//                        navController.navigate("deviceDetails")
//                    }
                    DiscoveredDeviceItem(device, heading, onClick = {
//                        navController.navigate("deviceDetails")
                        navController.navigate("deviceDetails/${device.device.address}")

                    })



                }
            }
        }
    }
}
