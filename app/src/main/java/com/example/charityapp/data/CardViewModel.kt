package com.example.charityapp.data

import java.time.LocalDateTime

data class VolunteerEventCard (
    val title: String? = null,
    val description: String? = null,
    val location: String? = null,
    val eventDate: LocalDateTime? = null,
    val goal: Int? = null,
    val raised: Int
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
    val goal: Int
)

