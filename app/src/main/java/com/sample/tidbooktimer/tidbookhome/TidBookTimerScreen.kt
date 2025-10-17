package com.sample.tidbooktimer.tidbookhome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sample.tidbooktimer.MyAppRoute
import com.sample.tidbooktimer.R
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
    val timerHistoryList = timerViewModel.timerHistoryList.collectAsStateWithLifecycle()

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
        onResume = { timerViewModel.resumeTimer() },
        getTimerHistoryList = { timerHistoryList.value },
        onLogout = {
            timerViewModel.logout {
                navController.navigate("sign_in") {
                    popUpTo(MyAppRoute.TidBookTimerHomeRoute) { inclusive = true }
                }
            }
        }
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
    getTimerHistoryList: () -> List<TimerEntry>,
    onLogout: () -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = androidx.compose.ui.res.painterResource(id = R.drawable.ic_background),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            alpha = 0.5f
        )

        TextButton(
            onClick = {}, modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            Text(
                text = stringResource(R.string.logout),
                color = Color.DarkGray,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                "Date: ${startTime?.toLocalDate() ?: LocalDate.now()}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                "Elapsed: ${(elapsedTime / 3600)}:${((elapsedTime % 3600) / 60)}:${(elapsedTime % 60)}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = Bold,
                color = if (isRunning) Color(0xFF4CAF50) else Color.Gray
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
                style = MaterialTheme.typography.titleLarge,
                fontWeight = Bold
            )

            HorizontalDivider(color = Color.Magenta.copy(0.5f), thickness = 1.dp)

            if (getTimerHistoryList().isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No timer history available.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray
                    )
                }

            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    // Header Row
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                        ) {
                            Text(
                                text = "Date",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "Start Time",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "End Time",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.End
                            )
                        }

                        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
                    }

                    // Data Rows
                    items(getTimerHistoryList()) { entry ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                        ) {
                            Text(
                                text = entry.date,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = entry.startTime,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = entry.endTime,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.End
                            )
                        }

                        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                    }
                }
            }
        }
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
        onResume = {},
        getTimerHistoryList = {
            listOf(
                TimerEntry("2024-06-01", "09:00:00", "10:00:00"),
                TimerEntry("2024-06-01", "11:00:00", "12:30:00"),
                TimerEntry("2024-06-02", "14:15:00", "15:45:00")
            )
        },
        onLogout = {}
    )
}