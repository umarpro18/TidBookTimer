package com.sample.tidbooktimer

import kotlinx.serialization.Serializable

@Serializable
sealed class MyAppRoute(val isTopBar: Boolean, val title: String = "") {
    @Serializable
    object SignInRoute : MyAppRoute(isTopBar = false)

    @Serializable
    object SignUpRoute : MyAppRoute(isTopBar = false)

    @Serializable
    object HomeRoute : MyAppRoute(isTopBar = true, "TidBook Timer")
}