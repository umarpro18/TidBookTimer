package com.sample.tidbooktimer.tidbookhome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun TidBookTimerScreen(
    navController: NavController,
    timerViewModel: TidBookTimerViewModel = hiltViewModel<TidBookTimerViewModel>()
) {
    val startTime = timerViewModel.startTime.collectAsStateWithLifecycle()
    val elapsedTime = timerViewModel.elapsedTime.collectAsStateWithLifecycle()
    val isRunning = timerViewModel.isRunning.collectAsStateWithLifecycle()

    LaunchedEffect(isRunning) {
        while (true) {
            delay(1000L)
            timerViewModel.tick()
        }
    }

    TidBookTimerScreenContent(
        startTime = startTime.value,
        elapsedTime = elapsedTime.value,
        isRunning = isRunning.value,
        onStart = { timerViewModel.startTimer() },
        onStop = { timerViewModel.stopTimer() },
        onPause = { timerViewModel.pauseTimer() },
        onResume = { timerViewModel.resumeTimer() }
    )
}

@Composable
private fun TidBookTimerScreenContent(
    startTime: LocalDateTime?,
    elapsedTime: Long,
    isRunning: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
) {
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            "Date: ${startTime?.toLocalDate() ?: LocalDate.now()}",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            "Elapsed: ${(elapsedTime / 3600)}:${((elapsedTime % 3600) / 60)}:${(elapsedTime % 60)}",
            style = MaterialTheme.typography.titleLarge
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onStart,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(2f)
                    .height(56.dp),
                enabled = !isRunning && startTime == null
            ) { Text("Start") }

            Button(
                onClick = onStop,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(2f)
                    .height(56.dp),
                enabled = startTime != null
            ) { Text("Stop") }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onPause,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                enabled = isRunning
            ) { Text("Pause") }

            Button(
                onClick = onResume,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                enabled = !isRunning && startTime != null
            ) { Text("Resume") }
        }
        Text(
            "Start Time: ${startTime?.toLocalTime()?.format(formatter) ?: "--:--:--"}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TidBookTimerScreenPreview() {
    TidBookTimerScreenContent(
        startTime = LocalDateTime.now(),
        elapsedTime = 3723, // 1 hour, 2 min, 3 sec
        isRunning = true,
        onStart = {},
        onStop = {},
        onPause = {},
        onResume = {}
    )
}