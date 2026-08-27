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
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.charityapp.data.CardViewModel
import com.example.charityapp.data.VolunteerCardInput
import com.example.charityapp.navigation.ROUTE_DASHBOARD
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Composable
fun UpdateCardScreen(navController: NavController, cardId: String) {
    val cardViewModel: CardViewModel = viewModel()
    val context = LocalContext.current
    val existingCard = remember { cardViewModel.getCardById(cardId) }

    if (existingCard == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            Text(text = "Card not found")
        }
        return
    }

    val dateTime = remember {
        Instant.ofEpochSecond(existingCard.eventDateEpochSeconds)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }

    var title by remember { mutableStateOf(existingCard.title ?: "") }
    var description by remember { mutableStateOf(existingCard.description ?: "") }
    var location by remember { mutableStateOf(existingCard.location ?: "") }
    var goalText by remember { mutableStateOf(existingCard.goal?.toString() ?: "0") }

    var year by remember { mutableStateOf(dateTime.year.toString()) }
    var month by remember { mutableStateOf(dateTime.monthValue.toString()) }
    var day by remember { mutableStateOf(dateTime.dayOfMonth.toString()) }
    var hour by remember { mutableStateOf(dateTime.hour.toString()) }

    val titleError = title.isBlank()
    val descriptionError = description.isBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(text = "Update volunteering event", style = MaterialTheme.typography.titleLarge, color = Color.White)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            isError = titleError,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            isError = descriptionError,
            minLines = 3,
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
        Spacer(Modifier.height(20.dp))

        Text(text = "Event date & time", style = MaterialTheme.typography.labelLarge, color = Color.White)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = year, onValueChange = { year = it }, label = { Text("Year") }, modifier = Modifier.weight(1f))
            OutlinedTextField(value = month, onValueChange = { month = it }, label = { Text("Month") }, modifier = Modifier.weight(1f))
            OutlinedTextField(value = day, onValueChange = { day = it }, label = { Text("Day") }, modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                val eventDate = LocalDateTime.of(
                    year.toIntOrNull() ?: 2026,
                    month.toIntOrNull() ?: 1,
                    day.toIntOrNull() ?: 1,
                    hour.toIntOrNull() ?: 0,
                    0
                )
                cardViewModel.updateCard(
                    cardId = cardId,
                    updatedData = VolunteerCardInput(
                        title = title,
                        description = description,
                        location = location,
                        eventDate = eventDate,
                        goal = goalText.toIntOrNull() ?: 0
                    ),
                    context = context,
                    onSuccess = { navController.navigate(ROUTE_DASHBOARD) }
                )
            },
            enabled = !titleError && !descriptionError,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Changes")
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UpdateCardScreenPreview(){
    UpdateCardScreen(navController = rememberNavController(), cardId = "cardId")
}
