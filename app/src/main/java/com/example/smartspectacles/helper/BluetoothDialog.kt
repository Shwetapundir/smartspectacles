package com.example.smartspectacles.helper

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import kotlin.system.exitProcess

@Composable
fun BluetoothDialog(onEnableClick: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(text = "Bluetooth Required", fontWeight = FontWeight.Bold)
        },
        text = {
            Text("This app needs Bluetooth enabled to scan nearby BLE devices.")
        },
        confirmButton = {
            TextButton(onClick = onEnableClick) {
                Text("Enable Bluetooth")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                // optional: exit or hide dialog
                exitProcess(0)
            }) {
                Text("Exit")
            }
        }
    )
}
