package com.example.qrcraft.app.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavigationGraphs {
    @Serializable
    data object ScanGraph : NavigationGraphs
}