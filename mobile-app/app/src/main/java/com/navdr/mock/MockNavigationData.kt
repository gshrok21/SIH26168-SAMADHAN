package com.navdr.mock

import com.navdr.model.NavigationUiState

data class SearchLocation(
    val title: String,
    val area: String,
    val distance: String,
    val category: String // "Recent", "Saved", "Current"
)

object MockNavigationData {

    val sampleLocations = listOf(
        SearchLocation("BHU Main Gate", "Varanasi", "4.2 km", "Recent"),
        SearchLocation("Varanasi Cantt Station", "Cantt, Varanasi", "5.8 km", "Recent"),
        SearchLocation("Assi Ghat", "Shivala, Varanasi", "3.1 km", "Saved"),
        SearchLocation("Lanka Crossing", "BHU Road, Varanasi", "3.9 km", "Recent"),
        SearchLocation("Sigra Complex", "Sigra, Varanasi", "2.5 km", "Saved"),
        SearchLocation("Godowlia Chowk", "Dashashwamedh, Varanasi", "4.6 km", "Recent"),
        SearchLocation("Durgakund Temple", "Durgakund, Varanasi", "3.4 km", "Saved")
    )

    val defaultUiState = NavigationUiState()
}
