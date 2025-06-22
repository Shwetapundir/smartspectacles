package com.example.smartspectacles.helper
import androidx.datastore.preferences.core.stringPreferencesKey

object UserPreferencesKeys {
    val TOKEN = stringPreferencesKey("user_token")
    val EMAIL = stringPreferencesKey("user_email")
    val NAME = stringPreferencesKey("user_name")
}
