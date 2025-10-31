package com.sample.tidbooktimer.data.model

import com.google.firebase.Timestamp

data class OrganizationDataModel(
    val orgId: String = "",
    val orgName: String = "",
    val timerEntry: List<TimerEntryDataModel> = emptyList()
)

data class OrganizationDetailModel(
    val orgDetails: Map<String, OrganizationDataModel> = mutableMapOf<String, OrganizationDataModel>(),
    val updateTime: Timestamp = Timestamp.now()
)