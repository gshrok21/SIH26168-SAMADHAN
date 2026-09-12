package com.navdr.mock

import com.navdr.model.TimelineEvent
import com.navdr.model.Trip

object MockTripData {

    val mockTrips = listOf(
        Trip(
            id = "TRIP-2026-001",
            origin = "Home",
            destination = "Office",
            date = "Today, 08:30 AM",
            totalDistanceKm = 8.4,
            durationMinutes = 24,
            gnssDistanceKm = 6.9,
            deadReckoningDistanceKm = 1.5,
            avgSpeedKmh = 32,
            maxEstimatedErrorMeters = 18.0,
            gnssCoveragePercent = 82,
            timeline = listOf(
                TimelineEvent("GNSS Available", "Strong satellite signal lock acquired", "00:00", "#10B981"),
                TimelineEvent("GNSS Weak", "Signal degradation detected near tunnel entrance", "08:15", "#F59E0B"),
                TimelineEvent("Dead Reckoning Active", "GNSS lost. Inertial sensors & AI model took over seamlessly", "10:30", "#06B6D4"),
                TimelineEvent("GNSS Recovered", "Satellite signal re-acquired upon exiting tunnel", "15:45", "#10B981"),
                TimelineEvent("Position Synchronized", "Smoothed sensor position aligned with GNSS telemetry", "16:00", "#10B981")
            )
        ),
        Trip(
            id = "TRIP-2026-002",
            origin = "Varanasi Cantt",
            destination = "BHU Main Gate",
            date = "Yesterday, 04:15 PM",
            totalDistanceKm = 5.8,
            durationMinutes = 18,
            gnssDistanceKm = 4.6,
            deadReckoningDistanceKm = 1.2,
            avgSpeedKmh = 28,
            maxEstimatedErrorMeters = 14.5,
            gnssCoveragePercent = 79,
            timeline = listOf(
                TimelineEvent("GNSS Available", "Satellites locked (18 in view)", "00:00", "#10B981"),
                TimelineEvent("Dead Reckoning Active", "Urban canyon signal black-out", "05:20", "#06B6D4"),
                TimelineEvent("Position Synchronized", "GNSS back online near Lanka", "11:10", "#10B981")
            )
        ),
        Trip(
            id = "TRIP-2026-003",
            origin = "Sigra",
            destination = "Assi Ghat",
            date = "10 Sep 2026, 06:45 PM",
            totalDistanceKm = 3.9,
            durationMinutes = 14,
            gnssDistanceKm = 3.9,
            deadReckoningDistanceKm = 0.0,
            avgSpeedKmh = 22,
            maxEstimatedErrorMeters = 4.0,
            gnssCoveragePercent = 100,
            timeline = listOf(
                TimelineEvent("GNSS Available", "100% continuous GNSS coverage", "00:00", "#10B981")
            )
        )
    )
}
