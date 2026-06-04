package com.example.qrcraft.app.presentation.navigation.graph

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.qrcraft.app.presentation.navigation.CustomNavType
import com.example.qrcraft.app.presentation.navigation.NavigationGraphs
import com.example.qrcraft.app.presentation.navigation.NavigationScreens
import com.example.qrcraft.core.data.model.BarModel
import com.example.qrcraft.feature.presentation.main.MainRoot
import com.example.qrcraft.feature.presentation.scan_result.ScanResultRoot
import com.example.qrcraft.feature.presentation.scan_result.ScanResultViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.reflect.typeOf

fun NavGraphBuilder.scanGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    navigation<NavigationGraphs.ScanGraph>(
        startDestination = NavigationScreens.Main
    ) {
        composable<NavigationScreens.Main> {
            MainRoot(
                modifier = modifier,
                navigateToScanResult = { barModel ->
                    navHostController.navigate(
                        NavigationScreens.ScanResult(
                            barModel = barModel
                        )
                    ) {
                        popUpTo(NavigationScreens.Main) {
                            inclusive = true
                            saveState = false
                        }
                    }
                }
            )
        }

        composable<NavigationScreens.ScanResult>(
            typeMap = mapOf(
                typeOf<BarModel>() to CustomNavType.BarModelNavType
            )
        ) { args ->
            val key = args.toRoute<NavigationScreens.ScanResult>()
            val scanResultViewModel: ScanResultViewModel = koinViewModel {
                parametersOf(key)
            }
            ScanResultRoot(
                modifier = modifier,
                scanResultViewModel = scanResultViewModel,
                navigateBack = {
                    navHostController.navigate(NavigationScreens.Main) {
                        popUpTo<NavigationScreens.ScanResult> {
                            inclusive = false
                            saveState = false
                        }
                    }
                }
            )
        }
    }
}