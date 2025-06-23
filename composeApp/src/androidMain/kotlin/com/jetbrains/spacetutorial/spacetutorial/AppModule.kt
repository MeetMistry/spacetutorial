package com.jetbrains.spacetutorial.spacetutorial

import com.jetbrains.spacetutorial.spacetutorial.cache.AndroidDatabaseDriverFactory
import com.jetbrains.spacetutorial.spacetutorial.network.SpaceXApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<SpaceXApi> { SpaceXApi() }
    single<SpaceXSDK> {
        SpaceXSDK(
            databaseDriverFactory = AndroidDatabaseDriverFactory(context = androidContext()), api = get()
        )
    }
    viewModel { RocketLaunchViewModel(sdk = get()) }
}