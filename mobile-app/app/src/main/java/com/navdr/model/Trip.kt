package com.navdr.model

data class TimelineEvent(
    val title: String,
    val description: String,
    val timeOffset: String,
    val statusColorHex: String
)

data class Trip(
    val id: String,
    val origin: String,
    val destination: String,
    val date: String,
    val totalDistanceKm: Double,
    val durationMinutes: Int,
    val gnssDistanceKm: Double,
    val deadReckoningDistanceKm: Double,
    val avgSpeedKmh: Int,
    val maxEstimatedErrorMeters: Double,
    val gnssCoveragePercent: Int,
    val timeline: List<TimelineEvent> = emptyList()
)
