package com.example.charityapp.ui.theme.screens.register.card

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.charityapp.data.VolunteerEventCard
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.material3.Button


@Composable
fun VolunteerEventCardItem(
    card: VolunteerEventCard,
    onVolunteerClick: (VolunteerEventCard) -> Unit,
    onDonateClick: (VolunteerEventCard) -> Unit,
    onEditClick: (VolunteerEventCard) -> Unit = {},
) {
    val isActive = card.eventDateEpochSeconds > Instant.now().epochSecond

    val containerColor = if (isActive) Color.DarkGray else Color(0xFF2A2A2A)
    val textColor = if (isActive) Color.White else Color.Gray
    val secondaryTextColor = if (isActive) Color.LightGray else Color.DarkGray

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = card.title ?: "Untitled", style = MaterialTheme.typography.titleMedium, color = textColor)
                Row {
                    IconButton(onClick = { onEditClick(card) }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Card", tint = textColor)
                    }
                    if (!isActive) {
                        Text(text = "Ended", style = MaterialTheme.typography.labelSmall, color = Color.Red)
                    }
                }
            }
            Spacer(Modifier.height(4.dp))
            Text(text = card.description ?: "", style = MaterialTheme.typography.bodyMedium, color = secondaryTextColor)
            Spacer(Modifier.height(8.dp))
            Text(text = card.location ?: "Location TBD", style = MaterialTheme.typography.labelMedium, color = textColor)

            if (card.eventDateEpochSeconds > 0) {
                val formattedDate = Instant.ofEpochSecond(card.eventDateEpochSeconds)
                    .atZone(ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern("MMM d, yyyy · h:mm a"))
                Text(text = formattedDate, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }

            Spacer(Modifier.height(8.dp))
            Text(text = "${card.raised} / ${card.goal} raised", style = MaterialTheme.typography.labelMedium, color = textColor)
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { onVolunteerClick(card) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Volunteer")
                }
                Button(
                    onClick = { onDonateClick(card) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Donate")
                }
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VolunteerEventCardItemPreview() {
    VolunteerEventCardItem(
        card = VolunteerEventCard(
            title = "Sample Event",
            description = "This is a sample event description.",
            location = "Sample Location",
            eventDateEpochSeconds = Instant.now().epochSecond,
            goal = 500,
            raised = 100
        ),
        onVolunteerClick = {},
        onDonateClick = {},
    )
}
