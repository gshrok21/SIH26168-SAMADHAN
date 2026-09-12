package com.navdr.navigation

object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val SEARCH = "search"
    const val ROUTE_PREVIEW = "route_preview"
    const val NAVIGATION = "navigation"
    const val HISTORY = "history"
    const val TRIP_DETAILS = "trip_details/{tripId}"
    const val SYSTEM = "system"
    const val SETTINGS = "settings"
    const val PERMISSIONS = "permissions"
    const val DEMO_MODE = "demo_mode"

    fun tripDetails(tripId: String) = "trip_details/$tripId"
}
