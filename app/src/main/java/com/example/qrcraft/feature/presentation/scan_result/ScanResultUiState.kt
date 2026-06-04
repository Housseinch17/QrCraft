package com.example.qrcraft.feature.presentation.scan_result

import com.example.qrcraft.core.data.model.BarModel
import com.example.qrcraft.core.data.model.ScannedContent
import com.google.mlkit.vision.barcode.common.Barcode

data class ScanResultUiState(
    val barModel: BarModel = BarModel(
        content = ScannedContent.TextContent("test"),
        rawValue = "",
        displayValue = "",
        format = Barcode.TYPE_TEXT
    )
)
