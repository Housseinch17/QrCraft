package com.example.qrcraft.core.data.model

import kotlinx.serialization.Serializable

@Serializable
data class BarModel(
    val content: ScannedContent,
    val rawValue: String,
    val displayValue: String?,
    val format: Int,
)