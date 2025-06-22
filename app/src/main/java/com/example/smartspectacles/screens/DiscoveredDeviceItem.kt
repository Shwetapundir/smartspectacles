package com.example.smartspectacles.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartspectacles.helper.DiscoveredDevice
import kotlin.math.pow

@Composable
fun DiscoveredDeviceItem(device: DiscoveredDevice, heading: Float, onClick: (DiscoveredDevice) -> Unit) {
    val distanceMeters = approximateDistance(device.rssi, -59, 2.2)
    val distanceString = if (distanceMeters < 1.0)
        String.format("%.1f cm", distanceMeters * 100)
    else
        String.format("%.2f m", distanceMeters)

    val distanceLabel = when {
        device.rssi > -60 -> "Very Close"
        device.rssi > -75 -> "Close"
        device.rssi > -90 -> "Far"
        else -> "Very Far"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(device) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Name: ${device.device.name ?: "Unknown"}")
            Text("Address: ${device.device.address}")
            Text("RSSI: ${device.rssi} dBm")
            Text("Estimated Distance: ~$distanceString")
            Text("Proximity: $distanceLabel")
            Text("Phone Heading: ${heading.toInt()}°")
        }
    }
}
fun approximateDistance(rssi: Int, txPower: Int, n: Double): Double {
    return 10.0.pow((txPower - rssi) / (10.0 * n))
}
