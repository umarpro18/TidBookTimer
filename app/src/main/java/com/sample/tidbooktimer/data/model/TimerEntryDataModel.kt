package com.sample.tidbooktimer.data.model

data class TimerEntryDataModel(
    val entryId: Int = 0,
    val date: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val elapsedTime: String = "",
    val totalPausedTime: String = ""
)


/* its with lat and long data for each action
data class TimerEntryDataModel(
    val entryId: Int = 0,
    val date: String = "",
    val elapsedTime: String = "",
    val totalPausedTime: String = "",
    val actions: List<TimerAction> = emptyList()
)

data class TimerAction(
    val type: ActionType,       // START, PAUSE, RESUME, STOP
    val time: String,           // e.g., "14:30"
    val latitude: Double,
    val longitude: Double
)

enum class ActionType {
    START,
    PAUSE,
    RESUME,
    STOP
}*/

/* data model
{
    "date": "2025-11-01",
    "elapsedTime": "00:01:21",
    "entryId": 414,
    "totalPausedTime": "00:00:39",
    "actions": [
    {
        "type": "START",
        "time": "15:11:20",
        "latitude": 59.334591,
        "longitude": 18.063240
    },
    {
        "type": "PAUSE",
        "time": "15:12:00",
        "latitude": 59.334700,
        "longitude": 18.063500
    },
    {
        "type": "RESUME",
        "time": "15:12:30",
        "latitude": 59.334800,
        "longitude": 18.063600
    },
    {
        "type": "STOP",
        "time": "15:13:20",
        "latitude": 59.334900,
        "longitude": 18.063700
    }
    ]
}*/

// to save
/*val timerEntryRef = Firebase
val timerEntryRef = FirebaseFirestore.getInstance()
    .collection("users")
    .document(uid)
    .collection("orgDetails")
    .document(orgId)
    .collection("timerEntries")
    .document(entryId.toString())
*/