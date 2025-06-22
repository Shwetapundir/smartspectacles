package com.example.smartspectacles.navigation

import android.app.Application
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smartspectacles.datasource.UserPreferences
import com.example.smartspectacles.helper.DevicesViewModelFactory
import com.example.smartspectacles.screens.BleScanLandingPage
import com.example.smartspectacles.screens.BluetoothScannerScreen
import com.example.smartspectacles.screens.DeviceDetailsScreen
import com.example.smartspectacles.screens.LoginScreen
import com.example.smartspectacles.screens.SplashScreen
import com.example.smartspectacles.viewmodel.DevicesViewModel
import org.w3c.dom.Text

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("splash") {
            val context = LocalContext.current
            val userPreferences = remember { UserPreferences(context) }
            SplashScreen(navController, userPreferences)
        }

        composable("login") { LoginScreen(navController) }
        composable("landingscreen") { BleScanLandingPage(navController) }
        composable("devicespage") {
            val context = LocalContext.current
            val viewModel: DevicesViewModel = viewModel (
                factory = DevicesViewModelFactory(context.applicationContext as Application)
            )
            BluetoothScannerScreen(navController,bluetoothViewModel = viewModel)
        }
//        composable("deviceDetails") {
//            val context = LocalContext.current
//            val viewModel: DevicesViewModel = viewModel(
//                factory = DevicesViewModelFactory(context.applicationContext as Application)
//            )
//            DeviceDetailsScreen(viewModel)
//        }

        composable("deviceDetails/{deviceAddress}") { backStackEntry ->
            val context = LocalContext.current
            val deviceAddress = backStackEntry.arguments?.getString("deviceAddress") ?: ""
            val viewModel: DevicesViewModel = viewModel(
                factory = DevicesViewModelFactory(context.applicationContext as Application)
            )

            val scannedDevices = viewModel.scannedDevices.collectAsState().value
            val selectedDevice = scannedDevices.find { it.device.address == deviceAddress }

            selectedDevice?.let {
                DeviceDetailsScreen(viewModel, it)
            } ?: Text("Device not found")
        }


    }

}

