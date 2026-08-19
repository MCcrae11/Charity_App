package com.example.charityapp.ui.theme.screens.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import com.example.charityapp.R
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.charityapp.navigation.ROUTE_REGISTER
import com.example.charityapp.navigation.ROUTE_SPLASH
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun SplashScreen(navController: NavController){

    LaunchedEffect(Unit) {
        delay(3000.milliseconds)
        navController.navigate(ROUTE_REGISTER){
            popUpTo (ROUTE_SPLASH){
                inclusive = true
            }
        }
    }
    Column(modifier = Modifier.fillMaxSize().background(
        Brush.verticalGradient(colors = listOf(Color(0xFFFFE0B2), Color(0xFFFFFFFF)) )
    ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {
        Image(
            painter = painterResource(id = R.drawable.charity),
            contentDescription = "App Logo",
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "CHARITY APP",
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            fontStyle = FontStyle.Normal,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Connecting hearts, transforming lives.",
            fontWeight = FontWeight.Light,
            fontSize = 16.sp,
            fontStyle = FontStyle.Italic,
            color = Color.DarkGray,
            letterSpacing = 2.0.sp,
            textDecoration = TextDecoration.Underline
        )
    }

}