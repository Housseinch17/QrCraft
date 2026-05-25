package com.example.qrcraft.feature.presentation.main

import com.example.qrcraft.core.data.model.BarScanState

data class MainUiState(
    val barScanState: BarScanState = BarScanState.Ideal,
    val isScanningBarcode: Boolean = false,
    val hasGrantCameraPermission: Boolean = true,
    val showSnackBar: Boolean = false,
)

