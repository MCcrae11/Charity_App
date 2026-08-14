package com.example.charityapp.ui.theme.screens.register.dashboard

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController){
    val selectedItem = remember { mutableIntStateOf(0) }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Charity App",
                                     fontSize = 25.sp,
                                     fontWeight = FontWeight.Bold,
                                     color = Color.White,
                                     modifier = Modifier.padding(horizontal = 100.dp, vertical = 12.dp))},
                      colors = TopAppBarDefaults.topAppBarColors(
                                containerColor= Color.Black,
                                titleContentColor = Color.Blue
                      )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.Black) {
                NavigationBarItem(
                    selected = selectedItem.intValue == 1,
                    onClick = { selectedItem.intValue = 1 },
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    label = { Text(text = "Home") }
                )
                NavigationBarItem(
                    selected = selectedItem.intValue == 2,
                    onClick = { selectedItem.intValue = 2 },
                    icon = { Icon(Icons.Filled.Add, contentDescription = "Add") },
                    label = { Text(text = "New") }
                )
                NavigationBarItem(
                    selected = selectedItem.intValue == 0,
                    onClick = { selectedItem.intValue = 0 },
                    icon = { Icon(Icons.Filled.AccountCircle, contentDescription = "Acount") },
                    label = { Text(text = "Account") }
                )
            }
        }
    )
    { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {

        }

    }

}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenPreview(){
    DashboardScreen(navController = rememberNavController())
}