package com.example.smartspectacles.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.smartspectacles.viewmodel.DevicesViewModel

class DevicesViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DevicesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DevicesViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
