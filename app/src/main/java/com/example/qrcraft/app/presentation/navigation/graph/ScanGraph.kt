package com.example.qrcraft.app.presentation.navigation.graph

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.qrcraft.app.presentation.navigation.NavigationGraphs
import com.example.qrcraft.app.presentation.navigation.NavigationScreens

fun NavGraphBuilder.scanGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    navigation<NavigationGraphs.ScanGraph>(
        startDestination = NavigationScreens.Main
    ) {
        composable<NavigationScreens.Main> {

        }

        composable<NavigationScreens.ScanResult> {

        }
    }
}