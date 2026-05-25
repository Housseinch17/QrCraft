package com.example.qrcraft.feature.presentation.main

import com.example.qrcraft.core.data.model.BarModel

sealed interface MainScreenActions {
    data object CloseApp : MainScreenActions
    data object GrantAccess : MainScreenActions
    data class UpdateCameraPermission(val hasGrantCameraPermission: Boolean): MainScreenActions
    data class UpdateIsScanningBarcode(val isScanningBarcode: Boolean): MainScreenActions
    data class OnBarcodeScanning(val barModel: BarModel): MainScreenActions
}