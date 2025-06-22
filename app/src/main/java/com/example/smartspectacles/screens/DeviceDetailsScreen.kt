package com.example.smartspectacles.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.smartspectacles.R
import com.example.smartspectacles.helper.DiscoveredDevice
import com.example.smartspectacles.viewmodel.DevicesViewModel

@Composable
fun DeviceDetailsScreen(viewModel: DevicesViewModel,device: DiscoveredDevice) {
//    val connectionState by viewModel.connectionState.collectAsState()
//    val batteryLevel by viewModel.batteryLevel.collectAsState()
    val heading by viewModel.heading.collectAsState()
//    val location by viewModel.location.collectAsState()

    Column (modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Smart Spectacles Tracker", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Card{
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Device Name: ${device.device.name ?: "Unknown"}")
                Text("Device Address: ${device.device.address}")
                Text("RSSI: ${device.rssi} dBm")
                Text("GPS: Active")
                Button(onClick = { viewModel.disconnect() }) {
                    Text("Disconnect")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Compass
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth(

            ).height(200.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img),
                contentDescription = null,
                modifier = Modifier.rotate(-heading)
            )
        }

        // Location
        Spacer(modifier = Modifier.height(16.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
//                Text("Latitude: ${location.latitude}")
//                Text("Longitude: ${location.longitude}")
//                Text("Last Updated: ${location.latitude}") // Simplified
                Text("Heading: ${heading.toInt()}")
            }
        }
    }
}
