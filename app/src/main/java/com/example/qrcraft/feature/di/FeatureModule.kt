package com.example.qrcraft.feature.di

import com.example.qrcraft.feature.presentation.main.MainScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureModule = module {
    viewModelOf(::MainScreenViewModel)
}