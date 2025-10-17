package com.sample.tidbooktimer.tidbookhome

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object TimerPreferences {
    val START_TIME = stringPreferencesKey("start_time")
    val ELAPSED_TIME = longPreferencesKey("elapsed_time")
    val IS_RUNNING = booleanPreferencesKey("is_running")
}