package com.example.charityapp.ui.theme.screens.register.account

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.charityapp.data.AuthViewModel
import com.example.charityapp.data.CardViewModel
import com.example.charityapp.navigation.ROUTE_LOGIN
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.charityapp.ui.theme.screens.register.card.AddCardScreen

@Composable
fun AccountScreen(navController: NavController) {
    val authViewModel: AuthViewModel = viewModel()
    val cardViewModel: CardViewModel = viewModel()
    val context = LocalContext.current
    val userId = authViewModel.currentUserId
    val volunteeredEvents = cardViewModel.getUserVolunteeredEvents()
    val donatedEvents = cardViewModel.getUserDonatedEvents()

    LaunchedEffect(userId) {
        cardViewModel.fetchCards()
        cardViewModel.fetchUserActivities(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "My Activity",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            if (volunteeredEvents.isNotEmpty()) {
                item {
                    Text(
                        text = "Volunteering",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF0D322B),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(volunteeredEvents) { (event, registration) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4F3))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = event.title ?: "Untitled", fontWeight = FontWeight.Bold)
                                Text(
                                    text = "Location: ${event.location ?: "TBD"}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            IconButton(onClick = {
                                cardViewModel.deleteVolunteerRegistration(registration.id, context) {
                                    cardViewModel.fetchUserActivities(userId)
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete registration",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
            }

            if (donatedEvents.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "Donations",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFFE69138),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(donatedEvents) { (event, prompt) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8F0))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = event.title ?: "Untitled", fontWeight = FontWeight.Bold)
                            Text(text = "Amount: KES ${prompt.amount}", color = Color(0xFFE69138), fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            if (volunteeredEvents.isEmpty() && donatedEvents.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No activity found.", color = Color.Gray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                navController.navigate(ROUTE_LOGIN) {
                    popUpTo(0)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text(text = "Logout", color = Color.White)
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AccountScreenPreview() {
    AccountScreen(navController = rememberNavController())
}
