package com.example.charityapp.ui.theme.screens.register.register


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.charityapp.R
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.charityapp.data.AuthViewModel


@Composable
fun RegisterScreen(navController: NavController){
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmpassword by remember { mutableStateOf("") }
    val authViewModel: AuthViewModel= viewModel()
    val context= LocalContext.current

    LazyColumn(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        item {
            Image(
                painter = painterResource(id = R.drawable.charity),
                contentDescription = "logo",
                modifier = Modifier
                    .size(250.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        }
        item {
            Text(
                text = "Sign Up",
                fontSize = 20.sp,
                fontStyle = Italic,
                color = MaterialTheme.colorScheme.primary
            )
        }
        item {
            Spacer(modifier = Modifier.height(15.dp))
        }
        item {
            OutlinedTextField(
                value = username,
                label = { Text(text = "Enter Name or Username") },
                onValueChange = { username = it },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
            )
        }
        item {
            Spacer(modifier = Modifier.height(15.dp))
        }
        item {
            OutlinedTextField(
                value = email,
                label = { Text(text = "Enter email") },
                onValueChange = { email = it },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) }
            )
        }
        item {
            Spacer(modifier = Modifier.height(15.dp))
        }
        item {
            OutlinedTextField(
                value = password,
                label = { Text(text = "Enter password") },
                onValueChange = { password = it },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) }
            )
        }
        item {
            Spacer(modifier = Modifier.height(15.dp))
        }
        item {
            OutlinedTextField(
                value = confirmpassword,
                label = { Text(text = "Confirm password") },
                onValueChange = { confirmpassword = it },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) }
            )
        }
        item {
            Spacer(modifier = Modifier.height(15.dp))
        }
        item {
            Button(
                onClick = {authViewModel.signup(
                    username = username,
                    email = email,
                    password = password,
                    confirmpassword = confirmpassword,
                    navController = navController,
                    context = context
                )}
            ) { Text(text = "Sign Up")}
        }
        item {
            Row() {
                Text(text = "Already Signed Up?")
                Text(text = "Login here")
            }
        }
    }
 }
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview(){
    RegisterScreen(navController = rememberNavController())
}