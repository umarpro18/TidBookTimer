package com.sample.tidbooktimer.tidbookhome

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class TidBookTimerViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    private val userId: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    private val timerRef = FirebaseDatabase.getInstance().getReference("user_timers/$userId")

    private val _startTime = MutableStateFlow<LocalDateTime?>(null)
    val startTime = _startTime.asStateFlow()

    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime = _elapsedTime.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning = _isRunning.asStateFlow()

    init {
        // Restore previous timer if app was killed
        restorePreviousTimer()
    }

    fun restorePreviousTimer() {
        viewModelScope.launch {
            val prefs = dataStore.data.first()
            prefs[TimerPreferences.START_TIME]?.let { startStr ->
                _startTime.value = LocalDateTime.parse(startStr, formatter)
                _elapsedTime.value = prefs[TimerPreferences.ELAPSED_TIME] ?: 0L
                _isRunning.value = prefs[TimerPreferences.IS_RUNNING] ?: false
            }
        }
    }

    fun startTimer() {
        if (_startTime.value == null) _startTime.value = LocalDateTime.now()
        _isRunning.value = true
        persistState()
    }

    fun pauseTimer() {
        _isRunning.value = false
        persistState()
    }

    fun resumeTimer() {
        _isRunning.value = true
        persistState()
    }

    fun stopTimer() {
        _startTime.value?.let { start ->
            val end = LocalDateTime.now()
            saveEntry(start, end)
        }
        _startTime.value = null
        _elapsedTime.value = 0
        _isRunning.value = false
        persistState()
    }

    fun tick() {
        if (_isRunning.value) {
            _elapsedTime.value += 1

            val now = LocalDateTime.now()
            _startTime.value?.let { start ->
                val endOfDay = LocalDateTime.of(start.toLocalDate(), LocalTime.of(23, 59, 0))

                // Auto-stop at 23:59
                if (now.isAfter(endOfDay)) {
                    saveEntry(start, endOfDay)

                    val totalElapsed = java.time.Duration.between(start, now).seconds
                    if (totalElapsed < 24 * 3600) {
                        _startTime.value =
                            LocalDateTime.of(start.toLocalDate().plusDays(1), LocalTime.MIDNIGHT)
                        _elapsedTime.value = 0
                    } else {
                        _startTime.value = null
                        _elapsedTime.value = 0
                        _isRunning.value = false
                    }
                    persistState()
                }

                // Safety max 24h
                if (now.isAfter(start.plusHours(24))) {
                    saveEntry(start, start.plusHours(24))
                    _startTime.value = null
                    _elapsedTime.value = 0
                    _isRunning.value = false
                    persistState()
                }
            }
        }
    }

    private fun saveEntry(start: LocalDateTime, end: LocalDateTime) {
        viewModelScope.launch {
            val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
            val entry = TimerEntry(
                date = start.toLocalDate().toString(),
                startTime = start.toLocalTime().format(formatter),
                endTime = end.toLocalTime().format(formatter)
            )
            timerRef.push().setValue(entry)
        }
    }

    private fun persistState() {
        viewModelScope.launch {
            dataStore.edit { prefs ->
                _startTime.value?.let { prefs[TimerPreferences.START_TIME] = it.format(formatter) }
                prefs[TimerPreferences.ELAPSED_TIME] = _elapsedTime.value
                prefs[TimerPreferences.IS_RUNNING] = _isRunning.value
            }
        }
    }
}

data class TimerEntry(
    val date: String = "",
    val startTime: String = "",
    val endTime: String = ""
)