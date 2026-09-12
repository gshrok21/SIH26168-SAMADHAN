package com.navdr.viewmodel

import androidx.lifecycle.ViewModel
import com.navdr.mock.MockTripData
import com.navdr.model.Trip
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
    private val _recentTrips = MutableStateFlow<List<Trip>>(MockTripData.mockTrips)
    val recentTrips: StateFlow<List<Trip>> = _recentTrips.asStateFlow()
}
