package com.example.qrcraft.feature.presentation.scan_result


sealed interface ScanResultActions {
    data object Share: ScanResultActions
    data object Copy: ScanResultActions
}