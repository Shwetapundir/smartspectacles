package com.example.smartspectacles.helper

import android.bluetooth.BluetoothDevice

data class DiscoveredDevice(
    val device: BluetoothDevice,
    val rssi: Int
)
