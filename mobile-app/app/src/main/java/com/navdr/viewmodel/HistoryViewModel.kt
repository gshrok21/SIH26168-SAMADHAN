package com.navdr.viewmodel

import androidx.lifecycle.ViewModel
import com.navdr.mock.MockTripData
import com.navdr.model.Trip
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HistoryViewModel : ViewModel() {
    private val _trips = MutableStateFlow<List<Trip>>(MockTripData.mockTrips)
    val trips: StateFlow<List<Trip>> = _trips.asStateFlow()

    fun getTripById(id: String): Trip? {
        return _trips.value.find { it.id == id } ?: _trips.value.firstOrNull()
    }
}
