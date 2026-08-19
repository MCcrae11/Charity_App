package com.example.charityapp.data

import android.content.Context
import android.util.Base64
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.charityapp.BuildConfig
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale

data class VolunteerEventCard(
    val id: String = "",
    val title: String? = null,
    val description: String? = null,
    val location: String? = null,
    val eventDateEpochSeconds: Long = 0L,
    val goal: Int? = null,
    val raised: Int = 0,
    val createdBy: String = ""
)

data class VolunteerCardInput(
    val title: String = "",
    val description: String = "",
    val location: String = "",
    val goalText: String = "",
    val year: String = "",
    val month: String = "",
    val day: String = "",
    val eventDate: LocalDateTime,
    val goal: Int,
    val createdBy: String = ""
)

data class VolunteerRegistration(
    val id: String = "",
    val eventId: String = "",
    val userId: String = "",
    val fullName: String = "",
    val mobileNumber: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class DonationPrompt(
    val eventId: String = "",
    val userId: String = "",
    val mobileNumber: String = "",
    val amount: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

class CardViewModel : ViewModel() {
    private val _cards = mutableStateOf<List<VolunteerEventCard>>(emptyList())
    val cards: State<List<VolunteerEventCard>> = _cards

    private val _userVolunteeredRegistrations = mutableStateOf<List<VolunteerRegistration>>(emptyList())
    private val _userDonatedPrompts = mutableStateOf<List<DonationPrompt>>(emptyList())

    private val mpesaApi: MpesaApi by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl("https://sandbox.safaricom.co.ke/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(MpesaApi::class.java)
    }

    fun fetchCards() {
        val dbRef = FirebaseDatabase.getInstance().getReference("Cards")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val cardList = mutableListOf<VolunteerEventCard>()
                for (cardSnapshot in snapshot.children) {
                    val card = cardSnapshot.getValue(VolunteerEventCard::class.java)
                    if (card != null) {
                        cardList.add(card.copy(id = cardSnapshot.key ?: ""))
                    }
                }
                _cards.value = cardList
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun fetchUserActivities(userId: String) {
        val volunteerRef = FirebaseDatabase.getInstance().getReference("EventVolunteers")
        volunteerRef.orderByChild("userId").equalTo(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val registrations = mutableListOf<VolunteerRegistration>()
                for (child in snapshot.children) {
                    val reg = child.getValue(VolunteerRegistration::class.java)
                    reg?.let { registrations.add(it.copy(id = child.key ?: "")) }
                }
                _userVolunteeredRegistrations.value = registrations
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        val donationRef = FirebaseDatabase.getInstance().getReference("DonationPrompts")
        donationRef.orderByChild("userId").equalTo(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val prompts = mutableListOf<DonationPrompt>()
                for (child in snapshot.children) {
                    val prompt = child.getValue(DonationPrompt::class.java)
                    prompt?.let { prompts.add(it) }
                }
                _userDonatedPrompts.value = prompts
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun getUserVolunteeredEvents(): List<Pair<VolunteerEventCard, VolunteerRegistration>> {
        val result = mutableListOf<Pair<VolunteerEventCard, VolunteerRegistration>>()
        for (reg in _userVolunteeredRegistrations.value) {
            val card = cards.value.find { it.id == reg.eventId }
            card?.let { result.add(it to reg) }
        }
        return result
    }

    fun getUserDonatedEvents(): List<Pair<VolunteerEventCard, DonationPrompt>> {
        val result = mutableListOf<Pair<VolunteerEventCard, DonationPrompt>>()
        for (prompt in _userDonatedPrompts.value) {
            val card = cards.value.find { it.id == prompt.eventId }
            card?.let { result.add(it to prompt) }
        }
        return result
    }

    fun getCardById(cardId: String): VolunteerEventCard? {
        return cards.value.find { it.id == cardId }
    }

    fun saveCard(
        card: VolunteerCardInput,
        userId: String,
        context: Context,
        onSuccess: () -> Unit
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Cards").push()
        val cardId = dbRef.key ?: ""
        
        val eventCard = VolunteerEventCard(
            id = cardId,
            title = card.title,
            description = card.description,
            location = card.location,
            eventDateEpochSeconds = card.eventDate.atZone(ZoneId.systemDefault()).toEpochSecond(),
            goal = card.goal,
            raised = 0,
            createdBy = userId
        )

        dbRef.setValue(eventCard).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Card saved successfully", Toast.LENGTH_SHORT).show()
                onSuccess()
            } else {
                Toast.makeText(context, "Failed to save card: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun updateCard(
        cardId: String,
        updatedData: VolunteerCardInput,
        context: Context,
        onSuccess: () -> Unit
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Cards").child(cardId)
        
        val updates = mapOf(
            "title" to updatedData.title,
            "description" to updatedData.description,
            "location" to updatedData.location,
            "eventDateEpochSeconds" to updatedData.eventDate.atZone(ZoneId.systemDefault()).toEpochSecond(),
            "goal" to updatedData.goal
        )

        dbRef.updateChildren(updates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Card updated successfully", Toast.LENGTH_SHORT).show()
                onSuccess()
            } else {
                Toast.makeText(context, "Failed to update card: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun deleteVolunteerRegistration(
        registrationId: String,
        context: Context,
        onSuccess: () -> Unit
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("EventVolunteers").child(registrationId)
        dbRef.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Registration cancelled.", Toast.LENGTH_SHORT).show()
                onSuccess()
            } else {
                Toast.makeText(context, "Failed to cancel registration: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun registerVolunteer(
        eventId: String,
        userId: String,
        fullName: String,
        mobileNumber: String,
        context: Context,
        onSuccess: () -> Unit
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("EventVolunteers").push()
        val registration = VolunteerRegistration(
            eventId = eventId,
            userId = userId,
            fullName = fullName,
            mobileNumber = mobileNumber
        )

        dbRef.setValue(registration).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
                onSuccess()
            } else {
                Toast.makeText(context, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun sendDonationPrompt(
        eventId: String,
        userId: String,
        mobileNumber: String,
        amount: String,
        context: Context,
        onSuccess: () -> Unit
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("DonationPrompts").push()
        val prompt = DonationPrompt(
            eventId = eventId,
            userId = userId,
            mobileNumber = mobileNumber,
            amount = amount
        )

        dbRef.setValue(prompt).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                initiateStkPush(mobileNumber, amount, context, onSuccess)
            } else {
                Toast.makeText(context, "Failed to record prompt: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initiateStkPush(
        phoneNumber: String,
        amount: String,
        context: Context,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                // 1. Generate Access Token
                val keys = "${BuildConfig.MPESA_CONSUMER_KEY}:${BuildConfig.MPESA_CONSUMER_SECRET}"
                val authHeader = "Basic " + Base64.encodeToString(keys.toByteArray(), Base64.NO_WRAP)
                
                val tokenResponse = mpesaApi.generateAccessToken(authHeader)
                if (tokenResponse.isSuccessful && (tokenResponse.body() != null)) {
                    val accessToken = tokenResponse.body()!!.accessToken
                    
                    // 2. Prepare STK Push
                    val timestamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
                    val shortCode = "174379" // Sandbox Shortcode
                    val passkey = BuildConfig.MPESA_PASSKEY
                    val password = Base64.encodeToString((shortCode + passkey + timestamp).toByteArray(), Base64.NO_WRAP)
                    
                    val formattedPhone = if (phoneNumber.startsWith("0")) {
                        "254" + phoneNumber.substring(1)
                    } else if (phoneNumber.startsWith("254")) {
                        phoneNumber
                    } else {
                        "254$phoneNumber"
                    }

                    val request = StkPushRequest(
                        businessShortCode = shortCode,
                        password = password,
                        timestamp = timestamp,
                        amount = amount,
                        partyA = formattedPhone,
                        partyB = shortCode,
                        phoneNumber = formattedPhone,
                        callBackURL = "https://mydomain.com/path", // Replace with your actual callback
                        accountReference = "CharityDonation",
                        transactionDesc = "Donation for event"
                    )
                    
                    val stkResponse = mpesaApi.stkPush("Bearer $accessToken", request)
                    if (stkResponse.isSuccessful) {
                        Toast.makeText(context, "Prompt sent to your phone!", Toast.LENGTH_LONG).show()
                        onSuccess()
                    } else {
                        Toast.makeText(context, "M-Pesa Error: ${stkResponse.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(context, "Auth Failed: ${tokenResponse.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Network Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
