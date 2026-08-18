package com.example.charityapp.ui.theme.screens.register.dashboard

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.charityapp.data.AuthViewModel
import com.example.charityapp.data.CardViewModel
import com.example.charityapp.data.VolunteerEventCard
import com.example.charityapp.ui.theme.screens.register.card.DonationPromptDialog
import com.example.charityapp.ui.theme.screens.register.card.VolunteerEventCardItem
import com.example.charityapp.ui.theme.screens.register.card.VolunteerRegistrationDialog
import java.time.Instant

@Composable
fun DashboardScreen(
    navController: NavController,
    cardViewModel: CardViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(),
) {
    val context = LocalContext.current
    var showVolunteerDialog by remember { mutableStateOf(false) }
    var showDonationDialog by remember { mutableStateOf(false) }
    var selectedCard by remember { mutableStateOf<VolunteerEventCard?>(null) }

    LaunchedEffect(Unit) {
        cardViewModel.fetchCards()
    }

    val cards by cardViewModel.cards
    val sortedCards = cards.sortedByDescending { it.eventDateEpochSeconds > Instant.now().epochSecond }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(sortedCards) { card ->
            VolunteerEventCardItem(
                card = card,
                onVolunteerClick = {
                    selectedCard = card
                    showVolunteerDialog = true
                },
                onDonateClick = {
                    selectedCard = card
                    showDonationDialog = true
                }
            )
        }
    }

    if (showVolunteerDialog && selectedCard != null) {
        VolunteerRegistrationDialog(
            onDismiss = { showVolunteerDialog = false },
            onRegister = { name, phone ->
                cardViewModel.registerVolunteer(
                    eventId = selectedCard!!.id,
                    userId = authViewModel.currentUserId,
                    fullName = name,
                    mobileNumber = phone,
                    context = context,
                    onSuccess = {
                        showVolunteerDialog = false
                    }
                )
            }
        )
    }

    if (showDonationDialog && selectedCard != null) {
        DonationPromptDialog(
            onDismiss = { showDonationDialog = false },
            onSendPrompt = { phone, amount ->
                cardViewModel.sendDonationPrompt(
                    eventId = selectedCard!!.id,
                    userId = authViewModel.currentUserId,
                    mobileNumber = phone,
                    amount = amount,
                    context = context,
                    onSuccess = {
                        showDonationDialog = false
                    }
                )
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(navController = rememberNavController())
}
