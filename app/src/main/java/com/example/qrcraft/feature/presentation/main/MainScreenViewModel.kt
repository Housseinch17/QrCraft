package com.example.qrcraft.feature.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrcraft.core.data.model.BarModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class MainScreenViewModel() : ViewModel() {
    private val _state = MutableStateFlow(MainUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<MainScreenEvents>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: MainScreenActions) {
        when (actions) {
            MainScreenActions.CloseApp -> closeApp()
            MainScreenActions.GrantAccess -> grantAccess()
            is MainScreenActions.UpdateCameraPermission -> updateCameraPermission(
                hasGrantCameraPermission = actions.hasGrantCameraPermission
            )

            is MainScreenActions.UpdateIsScanningBarcode -> updateIsScanningBarcode(
                isScanningBarcode = actions.isScanningBarcode
            )

            is MainScreenActions.OnBarcodeScanning -> onBarcodeScanning(
                barModel = actions.barModel
            )
        }
    }

    private fun closeApp() {
        viewModelScope.launch {
            _events.send(MainScreenEvents.CloseApp)
        }
    }

    private fun grantAccess() {
        viewModelScope.launch {
            _events.send(MainScreenEvents.RequestCameraPermission)
        }
    }

    private fun updateCameraPermission(hasGrantCameraPermission: Boolean) {
        _state.update { newState ->
            newState.copy(
                hasGrantCameraPermission = hasGrantCameraPermission,
            )
        }
        if (hasGrantCameraPermission) {
            showSnackBar()
        }
    }

    private fun updateIsScanningBarcode(isScanningBarcode: Boolean) {
        _state.update { newState ->
            newState.copy(
                isScanningBarcode = isScanningBarcode
            )
        }
    }

    private fun onBarcodeScanning(barModel: BarModel) {
        viewModelScope.launch {
            //show loader
            delay(2.seconds)
            _events.send(
                MainScreenEvents.NavigateToScanResult(barModel = barModel)
            )
        }
    }

    private fun showSnackBar() {
        viewModelScope.launch {
            _state.update { newState ->
                newState.copy(
                    showSnackBar = true,
                )
            }
            //show snackBar for 2 seconds
            delay(2.seconds)
            _state.update { newState ->
                newState.copy(
                    showSnackBar = false,
                )
            }
        }
    }

}