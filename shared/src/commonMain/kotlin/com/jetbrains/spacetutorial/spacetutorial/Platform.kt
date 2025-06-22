package com.jetbrains.spacetutorial.spacetutorial

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform