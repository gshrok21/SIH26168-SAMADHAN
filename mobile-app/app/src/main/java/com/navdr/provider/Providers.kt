package com.navdr.provider

import com.navdr.model.GnssStatus
import com.navdr.model.PositionMode
import com.navdr.model.SensorState
import kotlinx.coroutines.flow.StateFlow

interface PositionProvider {
    val positionMode: StateFlow<PositionMode>
    val confidence: StateFlow<Int>
    val accuracyMeters: StateFlow<Float>
}

interface GnssProvider {
    val gnssStatus: StateFlow<GnssStatus>
    val satelliteCount: StateFlow<Int>
}

interface SensorProvider {
    val sensorState: StateFlow<SensorState>
    val accelerometerValues: StateFlow<Triple<Float, Float, Float>>
    val gyroscopeValues: StateFlow<Triple<Float, Float, Float>>
    val magnetometerValues: StateFlow<Triple<Float, Float, Float>>
}

interface NavigationProvider {
    val distanceRemaining: StateFlow<String>
    val durationRemaining: StateFlow<String>
    val currentInstruction: StateFlow<String>
    val isNavigating: StateFlow<Boolean>
}
