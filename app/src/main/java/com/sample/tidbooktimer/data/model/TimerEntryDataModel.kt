package com.sample.tidbooktimer.data.model

data class TimerEntryDataModel(
    val entryId: Int = 0,
    val date: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val elapsedTime: String = "",
    val totalPausedTime: String = ""
)

/*data class TimerEntryDataModel(
    val entryId: Int = 0,
    val date: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val elapsedTime: String = "",
    val totalPausedTime: String = ""
)

data class OrgDetailModel(
    val name: String = "",
    val timerEntries: List<TimerEntryDataModel> = emptyList()
)

data class UserDataModel(
    val personalNumber: String = "",
    val email: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val orgDetails: Map<String, OrgDetailModel?> = emptyMap()
)*/