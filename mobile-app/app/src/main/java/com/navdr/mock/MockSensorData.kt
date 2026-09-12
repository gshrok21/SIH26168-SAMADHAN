package com.navdr.mock

data class SensorReading(
    val name: String,
    val status: String,
    val updateRateHz: Int,
    val accuracy: String,
    val hexColor: String
)

object MockSensorData {
    val sensorList = listOf(
        SensorReading("3-Axis Accelerometer", "Healthy", 100, "±0.01 m/s²", "#10B981"),
        SensorReading("3-Axis Gyroscope", "Healthy", 100, "±0.005 rad/s", "#10B981"),
        SensorReading("3-Axis Magnetometer", "Healthy", 50, "±0.2 µT", "#10B981"),
        SensorReading("GNSS Telemetry Receiver", "Strong (18 Satellites)", 10, "±4.0 m", "#10B981"),
        SensorReading("AI Inertial Dead-Reckoning Engine", "Ready", 50, "98% Confidence", "#06B6D4")
    )
}
