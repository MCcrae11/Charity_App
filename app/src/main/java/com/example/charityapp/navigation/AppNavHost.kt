package com.example.charityapp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.charityapp.ui.theme.screens.register.SplashScreen
import com.example.charityapp.ui.theme.screens.register.account.AccountScreen
import com.example.charityapp.ui.theme.screens.register.card.AddCardScreen
import com.example.charityapp.ui.theme.screens.register.card.UpdateCardScreen
import com.example.charityapp.ui.theme.screens.register.dashboard.DashboardScreen
import com.example.charityapp.ui.theme.screens.register.login.LoginScreen
import com.example.charityapp.ui.theme.screens.register.register.RegisterScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUTE_SPLASH
) {
    val selectedItem = remember { mutableIntStateOf(0) }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val showBars = currentRoute != ROUTE_REGISTER && currentRoute != ROUTE_SPLASH && currentRoute != ROUTE_LOGIN

    Scaffold(
        topBar = {
            if (showBars) {
                TopAppBar(
                    title = {
                        Text(
                            text = "Charity App",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 100.dp, vertical = 12.dp)
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Black,
                        titleContentColor = Color.Blue
                    )
                )
            }
        },
        bottomBar = {
            if (showBars) {
                NavigationBar(containerColor = Color.Black) {
                    NavigationBarItem(
                        selected = selectedItem.intValue == 1,
                        onClick = {
                            selectedItem.intValue = 1
                            navController.navigate(ROUTE_DASHBOARD)
                        },
                        icon = { Icon(imageVector = Icons.Filled.Home, contentDescription = "Home") },
                        label = { Text(text = "Home") }
                    )
                    NavigationBarItem(
                        selected = selectedItem.intValue == 2,
                        onClick = {
                            selectedItem.intValue = 2
                            navController.navigate(ROUTE_ADDCARD)
                        },
                        icon = { Icon(imageVector = Icons.Filled.Add, contentDescription = "Add") },
                        label = { Text(text = "New") }
                    )
                    NavigationBarItem(
                        selected = selectedItem.intValue == 0,
                        onClick = {
                            selectedItem.intValue = 0
                            navController.navigate(ROUTE_ACCOUNT)
                        },
                        icon = { Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = "Account") },
                        label = { Text(text = "Account") }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(ROUTE_REGISTER) { RegisterScreen(navController) }
            composable(ROUTE_LOGIN) { LoginScreen(navController) }
            composable (ROUTE_SPLASH) { SplashScreen(navController) }
            composable(ROUTE_DASHBOARD) { DashboardScreen(navController) }
            composable(ROUTE_ACCOUNT) { AccountScreen(navController) }
            composable("$ROUTE_UPDATECARD/{cardId}") { backStackEntry ->
                val cardId = backStackEntry.arguments?.getString("cardId") ?: ""
                UpdateCardScreen(navController, cardId)
            }
            composable(ROUTE_ADDCARD) { AddCardScreen(navController) }
        }
    }
}
