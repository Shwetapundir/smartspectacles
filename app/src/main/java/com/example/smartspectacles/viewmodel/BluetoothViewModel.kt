package com.example.smartspectacles.viewmodel

import android.app.Application
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel

class BluetoothViewModel(application: Application) : AndroidViewModel(application) {

    private val bluetoothManager =
        application.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

    private val _isBluetoothEnabled = mutableStateOf(true)
    val isBluetoothEnabled: State<Boolean> = _isBluetoothEnabled

    fun checkBluetoothStatus() {
        val adapter = bluetoothManager.adapter
        _isBluetoothEnabled.value = adapter?.isEnabled == true
    }
}
