package com.navdr.model

enum class SensorState(
    val label: String,
    val hexColor: String
) {
    HEALTHY("Healthy", "#10B981"),
    DEGRADED("Degraded", "#F59E0B"),
    UNAVAILABLE("Unavailable", "#EF4444")
}
