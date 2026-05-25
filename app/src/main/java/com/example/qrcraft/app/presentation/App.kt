package com.example.qrcraft.app.presentation

import android.app.Application
import com.example.qrcraft.BuildConfig
import com.example.qrcraft.app.di.appModule
import com.example.qrcraft.core.di.coreModule
import com.example.qrcraft.feature.di.featureModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {
    val applicationScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(tree = Timber.DebugTree())

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                appModule,
                coreModule,
                featureModule
            )
        }
    }
}