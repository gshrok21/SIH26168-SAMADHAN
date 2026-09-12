package com.navdr.viewmodel

import androidx.lifecycle.ViewModel
import com.navdr.model.DemoStage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DemoViewModel : ViewModel() {
    private val _currentStage = MutableStateFlow(DemoStage.NORMAL)
    val currentStage: StateFlow<DemoStage> = _currentStage.asStateFlow()

    fun selectStage(stage: DemoStage) {
        _currentStage.value = stage
    }
}
