package com.example.smartspectacles.network


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

// API Base URL
const val BASE_URL = "https://your-backend.com/api/"

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(
    val token: String,
    val name: String,
    val email: String,
    val preferencesFilled: Boolean // Backend response determines if preferences are filled
)
fun updateUserProfile(name: String, email: String, onSuccess: () -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        delay(2000) // Simulating API call delay
        onSuccess()
    }
}


interface LoginApiService {
    @POST("login")  // Replace with your actual login endpoint
    suspend fun login(@Body request: LoginRequest): LoginResponse
}

// Retrofit Instance
object ApiClient {
    val api: LoginApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LoginApiService::class.java)
    }
}
