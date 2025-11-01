package com.sample.tidbooktimer

import kotlinx.serialization.Serializable

@Serializable
sealed class MyAppRoute(val isTopBar: Boolean, val title: String = "") {
    @Serializable
    object SignInRoute : MyAppRoute(isTopBar = false)

    @Serializable
    object SignUpRoute : MyAppRoute(isTopBar = false)

    @Serializable
    data class StoreOrgRoute(val personalNumber: String) : MyAppRoute(isTopBar = false, "Store Org")

    @Serializable
    data class SelectOrgRoute(val orgNos: List<String> = emptyList<String>()): MyAppRoute(isTopBar = true, "Select Org")

    @Serializable
    data class TidBookTimerHomeRoute(
        val orgId: String = "",
        val orgName: String = ""
    ) : MyAppRoute(isTopBar = true, "TidBook Timer")

    @Serializable
    object AuthGraphRoute : MyAppRoute(isTopBar = false)

    @Serializable
    object TidBookTimerHomeGraphRoute : MyAppRoute(isTopBar = true, "TidBook Timer")
}