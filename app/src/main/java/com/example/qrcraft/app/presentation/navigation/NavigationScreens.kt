package com.example.qrcraft.app.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface NavigationScreens {
    @Serializable
    data object Main : NavigationScreens

    @Serializable
    data object ScanResult : NavigationScreens
}