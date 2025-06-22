package com.example.smartspectacles.viewmodel


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartspectacles.network.LoginResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: LoginResponse) : LoginState()
    data class Error(val message: String) : LoginState()
}

class LoginViewModel : ViewModel() {
    var loginState: LoginState = LoginState.Idle
        private set

//    fun login(email: String, password: String, onSuccess: (LoginResponse) -> Unit, onError: (String) -> Unit) {
//        viewModelScope.launch {
//            loginState = LoginState.Loading
//            try {
//                val response = ApiClient.api.login(LoginRequest(email, password))
//                loginState = LoginState.Success(response)
//                onSuccess(response)
//            } catch (e: Exception) {
//                loginState = LoginState.Error("Login failed: ${e.message}")
//                onError("Login failed: ${e.message}")
//            }
//        }
//    }
fun login(email: String, password: String, onSuccess: (LoginResponse) -> Unit, onError: (String) -> Unit) {
    viewModelScope.launch {
        loginState = LoginState.Loading
        delay(2000) // Simulate network delay

        if (email == "test" && password == "test") {
            val fakeUser = LoginResponse(
                token = "fake_token_123",
                name = "Test User",
                email = "test@example.com",
                preferencesFilled = false
            )
            loginState = LoginState.Success(fakeUser)
            onSuccess(fakeUser)
        } else {
            loginState = LoginState.Error("Invalid credentials")
            onError("Invalid credentials")
        }
    }
}

}
