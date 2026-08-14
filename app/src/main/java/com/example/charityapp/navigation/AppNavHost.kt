package com.example.charityapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.charityapp.ui.theme.screens.register.card.AddCardScreen
import com.example.charityapp.ui.theme.screens.register.dashboard.DashboardScreen
import com.example.charityapp.ui.theme.screens.register.register.RegisterScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController(),
               startDestination: String = ROUTE_REGISTER){
    NavHost(navController=navController,
            startDestination = startDestination)
    {
        composable (ROUTE_REGISTER) { RegisterScreen(navController) }
        composable (ROUTE_DASHBOARD) { DashboardScreen(navController) }
        composable (ROUTE_ADDCARD) { AddCardScreen(onSave = {
                 card ->
            navController.popBackStack(ROUTE_DASHBOARD, false) }) }
    }
}

