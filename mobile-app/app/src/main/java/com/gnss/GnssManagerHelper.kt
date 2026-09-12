package com.navdr.gnss

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.GnssStatus
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.core.content.ContextCompat

class GnssManagerHelper(
    context: Context,
    private val onGnssUpdate: (
        latitude: Double,
        longitude: Double,
        accuracyMeters: Float,
        satellitesInView: Int,
        satellitesUsedInFix: Int,
        gnssStatus: com.navdr.model.GnssStatus
    ) -> Unit
) {

    private val appContext = context.applicationContext

    private val locationManager =
        appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val mainHandler = Handler(Looper.getMainLooper())

    private var started = false

    private var satellitesInView = 0
    private var satellitesUsedInFix = 0

    private var lastLocation: Location? = null
    private var lastLocationTime = 0L

    private val locationListener = object : LocationListener {

        override fun onLocationChanged(location: Location) {
            lastLocation = location
            lastLocationTime = SystemClock.elapsedRealtime()

            sendUpdate()
        }

        override fun onProviderEnabled(provider: String) {
            // GPS provider enabled.
        }

        override fun onProviderDisabled(provider: String) {
            sendStatusUpdate(
                com.navdr.model.GnssStatus.GNSS_LOST
            )
        }

        @Suppress("DEPRECATION")
        override fun onStatusChanged(
            provider: String?,
            status: Int,
            extras: android.os.Bundle?
        ) {
            // Deprecated callback.
        }
    }

    private val gnssCallback = object : GnssStatus.Callback() {

        override fun onSatelliteStatusChanged(status: GnssStatus) {

            var inView = 0
            var usedInFix = 0

            for (i in 0 until status.satelliteCount) {

                inView++

                if (status.usedInFix(i)) {
                    usedInFix++
                }
            }

            satellitesInView = inView
            satellitesUsedInFix = usedInFix

            sendUpdate()
        }

        override fun onStarted() {
            // GNSS engine started.
        }

        override fun onStopped() {
            sendStatusUpdate(
                com.navdr.model.GnssStatus.GNSS_LOST
            )
        }

        override fun onFirstFix(ttffMillis: Int) {
            // First GNSS fix received.
        }
    }

    /*
     * Checks whether the last location update is too old.
     * If no fresh location is received for 5 seconds,
     * we consider GNSS lost.
     */
    private val statusChecker = object : Runnable {

        override fun run() {

            if (started) {

                val currentTime = SystemClock.elapsedRealtime()

                val timeSinceLastLocation =
                    currentTime - lastLocationTime

                if (lastLocation == null ||
                    timeSinceLastLocation > 5000L
                ) {

                    sendStatusUpdate(
                        com.navdr.model.GnssStatus.GNSS_LOST
                    )
                }

                mainHandler.postDelayed(this, 1000L)
            }
        }
    }

    private fun calculateGnssStatus(): com.navdr.model.GnssStatus {

        val location = lastLocation

        if (location == null) {
            return com.navdr.model.GnssStatus.GNSS_LOST
        }

        val timeSinceLocation =
            SystemClock.elapsedRealtime() - lastLocationTime

        // No fresh location for 5 seconds.
        if (timeSinceLocation > 5000L) {
            return com.navdr.model.GnssStatus.GNSS_LOST
        }

        /*
         * Weak GNSS conditions:
         * - fewer than 4 satellites used in the fix
         * OR
         * - accuracy worse than 20 meters
         */
        if (satellitesUsedInFix < 4 ||
            location.accuracy > 20f
        ) {
            return com.navdr.model.GnssStatus.GNSS_WEAK
        }

        return com.navdr.model.GnssStatus.GNSS_AVAILABLE
    }

    private fun sendUpdate() {

        val location = lastLocation ?: run {
            sendStatusUpdate(
                com.navdr.model.GnssStatus.GNSS_LOST
            )
            return
        }

        val status = calculateGnssStatus()

        onGnssUpdate(
            location.latitude,
            location.longitude,
            location.accuracy,
            satellitesInView,
            satellitesUsedInFix,
            status
        )
    }

    private fun sendStatusUpdate(
        status: com.navdr.model.GnssStatus
    ) {

        val location = lastLocation

        onGnssUpdate(
            location?.latitude ?: 0.0,
            location?.longitude ?: 0.0,
            location?.accuracy ?: 0f,
            satellitesInView,
            satellitesUsedInFix,
            status
        )
    }

    fun start() {

        if (started) return

        val fineLocationGranted =
            ContextCompat.checkSelfPermission(
                appContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationGranted =
            ContextCompat.checkSelfPermission(
                appContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        if (!fineLocationGranted && !coarseLocationGranted) {
            return
        }

        try {

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000L,
                0f,
                locationListener,
                Looper.getMainLooper()
            )

            locationManager.registerGnssStatusCallback(
                gnssCallback,
                mainHandler
            )

            started = true

            mainHandler.post(statusChecker)

        } catch (_: SecurityException) {

            started = false
        }
    }

    fun stop() {

        if (!started) return

        try {

            locationManager.removeUpdates(locationListener)

            locationManager.unregisterGnssStatusCallback(
                gnssCallback
            )

            mainHandler.removeCallbacks(statusChecker)

        } catch (_: Exception) {
        }

        started = false
    }
}