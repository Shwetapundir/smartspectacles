package com.example.smartspectacles

import android.Manifest
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.smartspectacles.helper.BluetoothDialog
import com.example.smartspectacles.navigation.AppNavGraph
import com.example.smartspectacles.viewmodel.BluetoothViewModel

class MainActivity : ComponentActivity() {

    private val bluetoothViewModel: BluetoothViewModel by viewModels()
    private val PERMISSION_REQUEST_CODE = 1001
    private val REQUEST_ENABLE_BT = 1002


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkAndRequestPermissions() // Check and request permissions
        bluetoothViewModel.checkBluetoothStatus()


        setContent {
            val navController = rememberNavController()
            val isBluetoothEnabled by bluetoothViewModel.isBluetoothEnabled

            Box(modifier = Modifier.fillMaxSize()) {
                AppNavGraph(navController)
                if (!isBluetoothEnabled) {
                    BluetoothDialog {
                        promptEnableBluetooth()
                    }
                }
            }
        }
    }
    private fun promptEnableBluetooth() {
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(intent, REQUEST_ENABLE_BT)
    }


    private fun checkAndRequestPermissions() {
        val permissionsToRequest = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.BLUETOOTH_SCAN)
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.BLUETOOTH_CONNECT)
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        // Request foreground permissions first
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), PERMISSION_REQUEST_CODE)
        } else {
            // If foreground permissions are granted, check and request background
            requestBackgroundLocationIfNeeded()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT) {
            bluetoothViewModel.checkBluetoothStatus()
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)} passing\n      in a {@link RequestMultiplePermissions} object for the {@link ActivityResultContract} and\n      handling the result in the {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            val allForegroundGranted = permissions
                .filterNot { it == Manifest.permission.ACCESS_BACKGROUND_LOCATION }
                .all { permission ->
                    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
                }

            if (allForegroundGranted) {
                requestBackgroundLocationIfNeeded()
            } else {
                showPermissionDialog()
            }
        }
    }
    private fun showPermissionDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permissions Required")
            .setMessage("You must grant all permissions to use this app.")
            .setCancelable(false)
            .setPositiveButton("Grant Permissions") { _, _ ->
                checkAndRequestPermissions()
            }
            .setNegativeButton("Exit App") { _, _ ->
                finish()
            }
            .show()
    }

    private fun requestBackgroundLocationIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                PERMISSION_REQUEST_CODE
            )
        }
    }

}
