package com.jetbrains.spacetutorial.spacetutorial

import com.jetbrains.spacetutorial.spacetutorial.cache.IOSDatabaseDriverFactory
import com.jetbrains.spacetutorial.spacetutorial.entity.RocketLaunch
import com.jetbrains.spacetutorial.spacetutorial.network.SpaceXApi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.dsl.module

class KoinHelper : KoinComponent {
    private val sdk: SpaceXSDK by inject<SpaceXSDK>()

    suspend fun getLaunches(forceReload: Boolean): List<RocketLaunch> {
        return sdk.getLaunches(forceReload = forceReload)
    }

    fun initKoin() {
        startKoin {
            modules(module {
                single<SpaceXApi> { SpaceXApi() }
                single<SpaceXSDK> {
                    SpaceXSDK(databaseDriverFactory = IOSDatabaseDriverFactory(), api = get())
                }
            })
        }
    }
}