package com.navdr.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class SensorManagerHelper(
    context: Context,
    private val onSensorUpdate: (
        accelX: Float,
        accelY: Float,
        accelZ: Float,
        gyroX: Float,
        gyroY: Float,
        gyroZ: Float,
        magX: Float,
        magY: Float,
        magZ: Float
    ) -> Unit
) : SensorEventListener {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val accelerometer =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val gyroscope =
        sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

    private val magnetometer =
        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    private var accelX = 0f
    private var accelY = 0f
    private var accelZ = 0f

    private var gyroX = 0f
    private var gyroY = 0f
    private var gyroZ = 0f

    private var magX = 0f
    private var magY = 0f
    private var magZ = 0f

    // UI ko har sensor event par update nahi karenge.
    // Screen update roughly every 200 ms hoga.
    private var lastUiUpdateTime = 0L

    fun start() {
        accelerometer?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_GAME
            )
        }

        gyroscope?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_GAME
            )
        }

        magnetometer?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_GAME
            )
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {

        when (event.sensor.type) {

            Sensor.TYPE_ACCELEROMETER -> {
                accelX = event.values[0]
                accelY = event.values[1]
                accelZ = event.values[2]
            }

            Sensor.TYPE_GYROSCOPE -> {
                gyroX = event.values[0]
                gyroY = event.values[1]
                gyroZ = event.values[2]
            }

            Sensor.TYPE_MAGNETIC_FIELD -> {
                magX = event.values[0]
                magY = event.values[1]
                magZ = event.values[2]
            }
        }

        // UI update ko throttle kar rahe hain.
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastUiUpdateTime >= 200) {
            lastUiUpdateTime = currentTime

            onSensorUpdate(
                accelX,
                accelY,
                accelZ,
                gyroX,
                gyroY,
                gyroZ,
                magX,
                magY,
                magZ
            )
        }
    }

    override fun onAccuracyChanged(
        sensor: Sensor?,
        accuracy: Int
    ) {
        // Sensor accuracy can be handled later if needed.
    }
}