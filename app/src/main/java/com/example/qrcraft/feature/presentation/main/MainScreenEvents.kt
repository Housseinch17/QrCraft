package com.example.qrcraft.feature.presentation.main

import com.example.qrcraft.core.data.model.BarModel

sealed interface MainScreenEvents {
    data object RequestCameraPermission: MainScreenEvents
    data object CloseApp: MainScreenEvents
    data class NavigateToScanResult(val barModel: BarModel) : MainScreenEvents
}