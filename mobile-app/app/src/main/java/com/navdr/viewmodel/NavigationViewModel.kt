package com.navdr.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.navdr.gnss.GnssManagerHelper
import com.navdr.model.DemoStage
import com.navdr.model.GnssStatus
import com.navdr.model.NavigationUiState
import com.navdr.model.PositionMode
import com.navdr.model.SensorState
import com.navdr.sensor.SensorManagerHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NavigationViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(NavigationUiState())
    val uiState: StateFlow<NavigationUiState> = _uiState.asStateFlow()

    // =========================
    // REAL SENSOR DATA
    // =========================

    private val sensorManagerHelper = SensorManagerHelper(
        application.applicationContext
    ) { accelX, accelY, accelZ,
        gyroX, gyroY, gyroZ,
        magX, magY, magZ ->

        _uiState.update { currentState ->
            currentState.copy(
                accelX = accelX,
                accelY = accelY,
                accelZ = accelZ,

                gyroX = gyroX,
                gyroY = gyroY,
                gyroZ = gyroZ,

                magX = magX,
                magY = magY,
                magZ = magZ,

                sensorHealth = SensorState.HEALTHY
            )
        }
    }

    // =========================
    // REAL GNSS / GPS DATA
    // =========================

    private val gnssManagerHelper = GnssManagerHelper(
    application.applicationContext
) { latitude,
    longitude,
    accuracyMeters,
    satellitesInView,
    satellitesUsedInFix,
    gnssStatus ->

    _uiState.update { currentState ->


        if (currentState.forceDeadReckoning) {
            return@update currentState
        }

        val banner = when (gnssStatus) {
            GnssStatus.GNSS_AVAILABLE -> null

            GnssStatus.GNSS_WEAK ->
                "GNSS Signal Weak — Positioning confidence reduced"

            GnssStatus.GNSS_LOST ->
                "GNSS Signal Lost — Dead Reckoning Ready"

            GnssStatus.GNSS_RECOVERED ->
                "GNSS Signal Recovered — Position synchronized"
        }

        currentState.copy(
    latitude = latitude,
    longitude = longitude,

    gnssAccuracyMeters = accuracyMeters,

    satellitesInView = satellitesInView,
    satellitesUsedInFix = satellitesUsedInFix,
    satellitesCount = satellitesInView,

    accuracyMeters =
        if (gnssStatus == GnssStatus.GNSS_LOST)
            currentState.accuracyMeters
        else
            accuracyMeters,

    gnssStatus = gnssStatus,

    positionMode =
        when {
            currentState.forceDeadReckoning ->
                PositionMode.DEAD_RECKONING

            gnssStatus == GnssStatus.GNSS_LOST ->
                PositionMode.DEAD_RECKONING

            else ->
                PositionMode.GNSS
        },

    bannerMessage =
        if (currentState.forceDeadReckoning) {
            "Force Dead Reckoning — GNSS Ignored"
        } else {
            banner
        }
)
    }
}

    init {
        // Start real phone sensors
        sensorManagerHelper.start()

        // Start real phone GNSS/GPS
        gnssManagerHelper.start()
    }

    override fun onCleared() {
        sensorManagerHelper.stop()
        gnssManagerHelper.stop()

        super.onCleared()
    }

    // =========================
    // NAVIGATION
    // =========================

    fun startNavigation(
        destinationName: String = "BHU Main Gate",
        destinationArea: String = "Varanasi"
    ) {
        _uiState.update {
            it.copy(
                isNavigating = true,
                destinationName = destinationName,
                destinationArea = destinationArea,
                distanceRemaining = "4.8 km",
                durationRemaining = "12 min",
                currentInstruction = "Turn right",
                currentRoad = "MG Road",
                distanceToNextTurn = "250 m"
            )
        }
    }

    fun stopNavigation() {
        _uiState.update {
            it.copy(isNavigating = false)
        }
    }

    // =========================
    // DEMO MODE
    // =========================

    fun setDemoStage(stage: DemoStage) {

        when (stage) {

            DemoStage.NORMAL -> {
                _uiState.update {
                    it.copy(
                        demoStage = DemoStage.NORMAL,
                        gnssStatus = GnssStatus.GNSS_AVAILABLE,
                        positionMode = PositionMode.GNSS,
                        positionConfidence = 98,
                        accuracyMeters = 4.0f,
                        sensorHealth = SensorState.HEALTHY,
                        bannerMessage = null
                    )
                }
            }

            DemoStage.WEAK_SIGNAL -> {
                _uiState.update {
                    it.copy(
                        demoStage = DemoStage.WEAK_SIGNAL,
                        gnssStatus = GnssStatus.GNSS_WEAK,
                        positionMode = PositionMode.GNSS,
                        positionConfidence = 84,
                        accuracyMeters = 8.5f,
                        satellitesCount = 7,
                        sensorHealth = SensorState.HEALTHY,
                        bannerMessage =
                            "GNSS Signal Weak — Positioning degrades slightly"
                    )
                }
            }

            DemoStage.GNSS_LOST -> {
                _uiState.update {
                    it.copy(
                        demoStage = DemoStage.GNSS_LOST,
                        gnssStatus = GnssStatus.GNSS_LOST,
                        positionMode = PositionMode.DEAD_RECKONING,
                        positionConfidence = 75,
                        accuracyMeters = 14.0f,
                        satellitesCount = 0,
                        sensorHealth = SensorState.HEALTHY,
                        distanceSinceGnssLost = "0.4 km",
                        drDuration = "00:45",
                        bannerMessage =
                            "GNSS Signal Lost — Dead Reckoning Active"
                    )
                }
            }

            DemoStage.DEAD_RECKONING -> {
                _uiState.update {
                    it.copy(
                        demoStage = DemoStage.DEAD_RECKONING,
                        gnssStatus = GnssStatus.GNSS_LOST,
                        positionMode = PositionMode.DEAD_RECKONING,
                        positionConfidence = 82,
                        accuracyMeters = 18.0f,
                        satellitesCount = 0,
                        sensorHealth = SensorState.HEALTHY,
                        distanceSinceGnssLost = "1.2 km",
                        drDuration = "02:14",
                        bannerMessage =
                            "Dead Reckoning Stable — Motion Sensors Active"
                    )
                }
            }

            DemoStage.GNSS_RECOVERED -> {
                _uiState.update {
                    it.copy(
                        demoStage = DemoStage.GNSS_RECOVERED,
                        gnssStatus = GnssStatus.GNSS_RECOVERED,
                        positionMode = PositionMode.HYBRID,
                        positionConfidence = 92,
                        accuracyMeters = 6.0f,
                        satellitesCount = 14,
                        sensorHealth = SensorState.HEALTHY,
                        bannerMessage =
                            "GNSS Signal Recovered — Position synchronized"
                    )
                }
            }

            DemoStage.SYNCHRONIZED -> {
                _uiState.update {
                    it.copy(
                        demoStage = DemoStage.SYNCHRONIZED,
                        gnssStatus = GnssStatus.GNSS_AVAILABLE,
                        positionMode = PositionMode.GNSS,
                        positionConfidence = 98,
                        accuracyMeters = 4.0f,
                        satellitesCount = 18,
                        sensorHealth = SensorState.HEALTHY,
                        bannerMessage = null
                    )
                }
            }
        }
    }

   fun setNavigationMode(mode: String) {
    _uiState.update {
        it.copy(navigationMode = mode)
    }
}

fun setForceDeadReckoning(enabled: Boolean) {

    _uiState.update { currentState ->

        currentState.copy(
            forceDeadReckoning = enabled,

            gnssStatus = if (enabled) {
                GnssStatus.GNSS_LOST
            } else {
                GnssStatus.GNSS_AVAILABLE
            },

            positionMode = if (enabled) {
                PositionMode.DEAD_RECKONING
            } else {
                PositionMode.GNSS
            },

            bannerMessage = if (enabled) {
                "Force Dead Reckoning — GNSS Ignored"
            } else {
                null
            }
        )
    }
}

}