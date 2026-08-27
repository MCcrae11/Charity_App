package com.example.charityapp.ui.theme.screens.register.card

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun DonationPromptDialog(
    onDismiss: () -> Unit,
    onSendPrompt: (String, String) -> Unit,
) {
    var mobileNumber by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFFDFCF7),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Send a donation prompt",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D322B)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Enter the mobile number that should receive the payment prompt.",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Mobile number",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray
                )
                OutlinedTextField(
                    value = mobileNumber,
                    onValueChange = { mobileNumber = it },
                    placeholder = { Text("0712 345 678", color = Color.LightGray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFE69138),
                        unfocusedBorderColor = Color.LightGray
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Amount (KES)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray
                )
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it.filter { c -> c.isDigit() } },
                    placeholder = { Text("100", color = Color.LightGray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFE69138),
                        unfocusedBorderColor = Color.LightGray
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onSendPrompt(mobileNumber, amount) },
                    enabled = mobileNumber.isNotBlank() && amount.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE69138)
                    )
                ) {
                    Text(text = "Send prompt", color = Color.White, fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "Cancel",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DonationPromptDialogPreview() {
    DonationPromptDialog(onDismiss = {}, onSendPrompt = { _, _ -> })
}
