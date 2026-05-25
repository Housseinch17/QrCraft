package com.example.qrcraft.app.presentation.navigation

import com.example.qrcraft.core.data.model.BarModel
import kotlinx.serialization.Serializable

@Serializable
sealed interface NavigationScreens {
    @Serializable
    data object Main : NavigationScreens

    @Serializable
    data class ScanResult(val barModel: BarModel) : NavigationScreens
}