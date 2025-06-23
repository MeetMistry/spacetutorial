package com.jetbrains.spacetutorial.spacetutorial

import com.jetbrains.spacetutorial.spacetutorial.cache.Database
import com.jetbrains.spacetutorial.spacetutorial.cache.DatabaseDriverFactory
import com.jetbrains.spacetutorial.spacetutorial.entity.RocketLaunch
import com.jetbrains.spacetutorial.spacetutorial.network.SpaceXApi

class SpaceXSDK(databaseDriverFactory: DatabaseDriverFactory, val api: SpaceXApi) {
    private val database = Database(databaseDriverFactory)

    @Throws(Exception::class)
    suspend fun getLaunches(forceReload: Boolean): List<RocketLaunch> {
        val cachedLaunches = database.getAllLaunches()
        return if (cachedLaunches.isNotEmpty() && !forceReload) {
            cachedLaunches
        } else {
            api.getAllLaunches().also {
                database.clearAndCreateLaunches(it)
            }
        }
    }
}