package com.navdr.model

enum class PositionMode(
    val title: String,
    val description: String
) {
    GNSS("GNSS Satellite", "Satellite constellation lock active"),
    DEAD_RECKONING("Dead Reckoning Active", "Navigation continuing using motion sensors and intelligent positioning"),
    HYBRID("Hybrid Synchronization", "Fusing satellite telemetry with inertial motion model")
}
