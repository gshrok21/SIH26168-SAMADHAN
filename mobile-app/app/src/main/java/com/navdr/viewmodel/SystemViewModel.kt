package com.navdr.viewmodel

import androidx.lifecycle.ViewModel
import com.navdr.mock.MockSensorData
import com.navdr.mock.SensorReading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SystemViewModel : ViewModel() {
    private val _sensors = MutableStateFlow<List<SensorReading>>(MockSensorData.sensorList)
    val sensors: StateFlow<List<SensorReading>> = _sensors.asStateFlow()
}
