package com.example.smartspectacles.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.smartspectacles.R
import com.example.smartspectacles.datasource.UserPreferences

@Composable
fun BleScanLandingPage(navController: NavController) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val user by userPreferences.userFlow.collectAsState(initial = null)

    LaunchedEffect(user) {
        user?.let {
            Log.d("UserToken",it.token)
            // Use user.email, user.name, etc.
        }
    }
    Surface (modifier = Modifier.fillMaxSize(), color = Color(0xFFF5F5F5)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = buildAnnotatedString {
                    append("BLE Scan App")
                    user?.let {
                        append(" - ${it.name}") // You can use it.email if you prefer
                    }
                },
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E88E5)
            )

            Text(
                text = "Easily discover and connect to Bluetooth Low Energy devices around you.",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                color = Color.DarkGray,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Button (
                onClick = {
                    navController.navigate("devicespage")
                          },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .height(50.dp)
                    .width(200.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF1E88E5))
            ) {
                Text("Start Scanning", color = Color.White, fontSize = 16.sp)
            }

            Card (
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth(0.85f)
                    .aspectRatio(16f / 9f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.download),
                    contentDescription = "App Preview",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
