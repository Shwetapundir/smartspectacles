package com.example.smartspectacles.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.smartspectacles.R
import com.example.smartspectacles.datasource.UserPreferences
import com.example.smartspectacles.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel = viewModel()) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userPreferences = remember { UserPreferences(context) }
    var isLogin by remember { mutableStateOf(true) } // Toggle between login/signup
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background), // Ensure app_logo is in res/drawable
            contentDescription = "App Logo",
            modifier = Modifier
                .clip(CircleShape)
//                .border(2.dp, Color.Gray, CircleShape)
                .size(100.dp)
                .padding(bottom = 16.dp)
        )
        Text(
            text = if (isLogin) "Login" else "Sign Up",
            fontSize = 28.sp,
            color = Color.Black,


        )

        Spacer(modifier = Modifier.height(20.dp))

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Login / Sign Up Button
        Button(
            onClick = {
                isLoading = true
                loginViewModel.login(email, password,
                    onSuccess = { user ->
//                        if (user.preferencesFilled) {
//                            navController.navigate("home/${user.name}/${user.email}") // Go directly to home
//                        } else {
//                            navController.navigate("preferences/${user.name}/${user.email}") // Go to preferences first
//                        }
                        coroutineScope.launch {
                            userPreferences.saveUser(user)
                            isLoading = false
                            navController.navigate("landingscreen")
                        }

                       navController.navigate("landingscreen")
                    },
                    onError = { error ->
                        isLoading = false
                        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                    }
                )
            },
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Login")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Toggle between Login & Signup
        TextButton(onClick = { isLogin = !isLogin }) {
            Text(text = if (isLogin) "Don't have an account? Sign Up" else "Already have an account? Login")

        }
    }

}
