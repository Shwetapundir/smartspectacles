package com.example.smartspectacles.datasource

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.smartspectacles.helper.UserPreferencesKeys
import com.example.smartspectacles.network.LoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_prefs")
class UserPreferences(context: Context) {

    private val dataStore = context.dataStore

    suspend fun saveUser(user: LoginResponse) {
        dataStore.edit { prefs ->
            prefs[UserPreferencesKeys.TOKEN] = user.token
            prefs[UserPreferencesKeys.EMAIL] = user.email
            prefs[UserPreferencesKeys.NAME] = user.name
        }
    }

    val userFlow: Flow<LoginResponse?> = dataStore.data
        .catch { e ->
            Log.e("UserPreferences", "DataStore error: ${e.message}")
            emit(emptyPreferences())
        }
        .map { prefs ->
            val token = prefs[UserPreferencesKeys.TOKEN]
            val email = prefs[UserPreferencesKeys.EMAIL]
            val name = prefs[UserPreferencesKeys.NAME]

            if (!token.isNullOrEmpty() && !email.isNullOrEmpty() && !name.isNullOrEmpty()) {
                LoginResponse(token, name, email, false)
            } else {
                null
            }
        }


    suspend fun clearUser() {
        dataStore.edit { it.clear() }
    }
}
