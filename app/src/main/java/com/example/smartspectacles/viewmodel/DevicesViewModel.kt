package com.example.smartspectacles.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartspectacles.helper.DiscoveredDevice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DevicesViewModel(application: Application) : AndroidViewModel(application) {
    // GPS and Heading data
    private val _location = MutableStateFlow<Location?>(null)
    val location: StateFlow<Location?> = _location

    private val _heading = MutableStateFlow<Float>(0f)
    val heading: StateFlow<Float> = _heading


    private val _scannedDevices = MutableStateFlow<List<DiscoveredDevice>>(emptyList())
    val scannedDevices: StateFlow<List<DiscoveredDevice>> = _scannedDevices

    private val bluetoothAdapter: BluetoothAdapter? =
        (application.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    private var bluetoothGatt: BluetoothGatt? = null
    private val _connectedDevice = MutableStateFlow<BluetoothDevice?>(null)
    val connectedDevice: StateFlow<BluetoothDevice?> = _connectedDevice

    private val bluetoothReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE).toInt()
                    device?.let {
                        val discoveredDevice = DiscoveredDevice(it, rssi)
                        viewModelScope.launch {
                            _scannedDevices.value = _scannedDevices.value + discoveredDevice
                        }
                        Log.d("BluetoothScan", "Found: ${it.name} (${it.address}) RSSI: $rssi")
                    }
                }
            }
        }
    }

    fun startScan(context: Context) {
        if (bluetoothAdapter?.isEnabled == false) {
            Log.d("BluetoothScan", "Bluetooth is disabled")
            return
        }

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        context.registerReceiver(bluetoothReceiver, filter)

        bluetoothAdapter?.startDiscovery()
        Log.d("BluetoothScan", "Scan started")
    }

//    fun stopScan(context: Context) {
//        bluetoothAdapter?.cancelDiscovery()
//        context.unregisterReceiver(bluetoothReceiver)
//        Log.d("BluetoothScan", "Scan stopped")
//    }
fun stopScan(context: Context) {
    bluetoothAdapter?.cancelDiscovery()
    try {
        context.unregisterReceiver(bluetoothReceiver)
    } catch (e: IllegalArgumentException) {
        Log.w("BluetoothScan", "Receiver was not registered: ${e.message}")
    }

    _scannedDevices.value = emptyList() // Clear the list here
    Log.d("BluetoothScan", "Scan stopped and devices cleared")
}


    @SuppressLint("MissingPermission")
    fun connectToDevice(device: BluetoothDevice) {
        stopScan(getApplication()) // Stop scanning before connecting

        bluetoothGatt = device.connectGatt(getApplication(), false, object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.d("BluetoothConnect", "Connected to ${device.address}")
                    _connectedDevice.value = device
                    gatt.discoverServices()
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    Log.d("BluetoothConnect", "Disconnected from ${device.address}")
                    _connectedDevice.value = null
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Log.d("BluetoothConnect", "Services discovered for ${device.address}")
                    // You can access services and characteristics here
                    val services = gatt.services
                    services.forEach {
                        Log.d("BluetoothService", "Service: ${it.uuid}")
                    }
                }
            }
        })

        Log.d("BluetoothConnect", "Connecting to ${device.name} (${device.address})")
    }
    fun disconnect() {
        bluetoothGatt?.disconnect()
        bluetoothGatt?.close()
        bluetoothGatt = null
        _connectedDevice.value = null
        Log.d("BluetoothConnect", "Device disconnected")
    }

}
