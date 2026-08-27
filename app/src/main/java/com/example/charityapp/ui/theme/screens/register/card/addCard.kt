package com.example.charityapp.ui.theme.screens.register.card


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.charityapp.data.AuthViewModel
import com.example.charityapp.data.CardViewModel
import com.example.charityapp.data.VolunteerCardInput
import com.example.charityapp.data.VolunteerEventCard
import com.example.charityapp.navigation.ROUTE_DASHBOARD
import java.time.LocalDateTime
import java.time.ZoneId

@Composable
fun AddCardScreen(navController: NavController) {
    val authViewModel: AuthViewModel = viewModel()
    val cardViewModel: CardViewModel = viewModel()
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var goalText by remember { mutableStateOf("200") }

    var year by remember { mutableStateOf("2026") }
    var month by remember { mutableStateOf("8") }
    var day by remember { mutableStateOf("30") }
    var hour by remember { mutableStateOf("9") }

    val titleError = title.isBlank()
    val descriptionError = description.isBlank()

    val eventDate by remember {
        derivedStateOf {
            LocalDateTime.of(
                year.toIntOrNull() ?: 2026,
                (month.toIntOrNull() ?: 1).coerceIn(1, 12),
                (day.toIntOrNull() ?: 1).coerceIn(1, 31),
                (hour.toIntOrNull() ?: 0).coerceIn(0, 23),
                0
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(text = "Create volunteering event", style = MaterialTheme.typography.titleLarge, color = Color.White)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            isError = titleError,
            supportingText = { if (titleError) Text("Title is required") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            isError = descriptionError,
            supportingText = { if (descriptionError) Text("Description is required") },
            minLines = 3,
            maxLines = 5,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = goalText,
            onValueChange = { goalText = it.filter { c -> c.isDigit() } },
            label = { Text("Volunteer / donor goal") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        Text(text = "Event date & time", 
            style = MaterialTheme.typography.labelLarge, 
            color = Color.White)
        Spacer(Modifier.height(6.dp))
        
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = year, 
                onValueChange = { year = it.filter { c -> c.isDigit() } },
                label = { Text("Year") }, 
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = month, 
                onValueChange = { month = it.filter { c -> c.isDigit() } },
                label = { Text("Month") }, 
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = day, 
                onValueChange = { day = it.filter { c -> c.isDigit() } },
                label = { Text("Day") }, 
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = hour, 
                onValueChange = { hour = it.filter { c -> c.isDigit() } },
                label = { Text("Hour") }, 
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                val cardInput = VolunteerCardInput(
                    title = title,
                    description = description,
                    location = location,
                    eventDate = eventDate,
                    goal = goalText.toIntOrNull() ?: 0
                )
                cardViewModel.saveCard(
                    card = cardInput,
                    userId = authViewModel.currentUserId,
                    context = context,
                    onSuccess = {
                        navController.navigate(ROUTE_DASHBOARD) {
                            popUpTo(ROUTE_DASHBOARD) { inclusive = true }
                        }
                    }
                )
            },
            enabled = !titleError && !descriptionError,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save card")
        }

        Spacer(Modifier.height(28.dp))

        VolunteerEventCardItem(
            card = VolunteerEventCard(
                title = title.ifBlank { "Untitled event" },
                description = description.ifBlank { "Add a description for your event." },
                location = location.ifBlank { "Location TBD" },
                eventDateEpochSeconds = eventDate.atZone(ZoneId.systemDefault()).toEpochSecond(),
                goal = goalText.toIntOrNull() ?: 0,
                raised = 0
            ),
            onVolunteerClick = {},
            onDonateClick = {}
        )
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddCardScreenPreview() {
    AddCardScreen(navController = rememberNavController())
}
