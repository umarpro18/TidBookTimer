package com.sample.tidbooktimer.data.model

data class TimerEntryDataModel(
    val entryId: Int = 0,
    val date: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val elapsedTime: String = "",
    val totalPausedTime: String = ""
)
