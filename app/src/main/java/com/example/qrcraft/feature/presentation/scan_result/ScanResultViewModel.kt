package com.example.qrcraft.feature.presentation.scan_result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrcraft.app.presentation.navigation.NavigationScreens
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScanResultViewModel(
    private val key: NavigationScreens.ScanResult
) : ViewModel() {
    private val _state = MutableStateFlow(ScanResultUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<ScanResultEvents>()
    val events = _events.receiveAsFlow()

    init {
        _state.update { newState ->
            newState.copy(
                barModel = key.barModel
            )
        }
    }

    fun onActions(scanResultActions: ScanResultActions) {
        when (scanResultActions) {
            ScanResultActions.Copy -> copy()
            ScanResultActions.Share -> share()
        }
    }

    private fun copy() {
        viewModelScope.launch {
            val rawValue = _state.value.barModel.rawValue
            _events.send(ScanResultEvents.Copy(rawValue = rawValue))
        }
    }

    private fun share() {
        viewModelScope.launch {
            val rawValue = _state.value.barModel.rawValue
            _events.send(ScanResultEvents.Share(rawValue = rawValue))
        }
    }

}