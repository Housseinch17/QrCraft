package com.example.qrcraft.app.di

import android.content.Context
import com.example.qrcraft.app.presentation.App
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single<Context> { androidApplication().applicationContext }
    single<CoroutineScope> {
        (androidApplication() as App).applicationScope
    }
}