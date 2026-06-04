package com.example.qrcraft.feature.presentation.scan_result

sealed interface ScanResultEvents {
    data class Copy(val rawValue: String) : ScanResultEvents
    data class Share(val rawValue: String) : ScanResultEvents
}