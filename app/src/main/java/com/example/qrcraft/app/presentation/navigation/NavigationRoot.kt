package com.example.qrcraft.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.qrcraft.app.presentation.navigation.graph.scanGraph

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = NavigationGraphs.ScanGraph
    ) {
        scanGraph(navHostController = navHostController)
    }
}