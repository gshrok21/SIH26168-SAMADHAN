package com.navdr.model

enum class DemoStage {
    NORMAL,
    WEAK_SIGNAL,
    GNSS_LOST,
    DEAD_RECKONING,
    GNSS_RECOVERED,
    SYNCHRONIZED
}

data class NavigationUiState(
    val gnssStatus: GnssStatus = GnssStatus.GNSS_AVAILABLE,
    val positionMode: PositionMode = PositionMode.GNSS,
    val positionConfidence: Int = 98,
    val accuracyMeters: Float = 4.0f,
    val distanceRemaining: String = "4.8 km",
    val durationRemaining: String = "12 min",
    val currentInstruction: String = "Turn right",
    val currentRoad: String = "MG Road",
    val distanceToNextTurn: String = "250 m",
    val estimatedSpeedKmh: Int = 32,
    val sensorHealth: SensorState = SensorState.HEALTHY,
    val isNavigating: Boolean = false,
    val isDemoMode: Boolean = true,
    val demoStage: DemoStage = DemoStage.NORMAL,
    val distanceSinceGnssLost: String = "1.2 km",
    val drDuration: String = "02:14",
    val satellitesCount: Int = 18,
    val forceDeadReckoning: Boolean = false,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val gnssAccuracyMeters: Float = 0f,
    val satellitesInView: Int = 0,
    val satellitesUsedInFix: Int = 0,
    val accelX: Float = 0.12f,
    val accelY: Float = 9.71f,
    val accelZ: Float = 0.31f,
    val gyroX: Float = 0.02f,
    val gyroY: Float = 0.01f,
    val gyroZ: Float = 0.04f,
    val magX: Float = 24.2f,
    val magY: Float = -8.1f,
    val magZ: Float = 41.5f,
    val destinationName: String = "BHU Main Gate",
    val destinationArea: String = "Varanasi",
    val batteryPercent: Int = 82,
    val navigationMode: String = "Driving", // Driving, Walking, Cycling
    val bannerMessage: String? = null
)
