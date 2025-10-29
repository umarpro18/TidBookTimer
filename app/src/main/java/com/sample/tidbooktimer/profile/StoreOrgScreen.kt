package com.sample.tidbooktimer.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun StoreOrgScreen(viewModel: StoreOrgViewModel, personalNumber: String) {
    StoreOrgContentScreen(personalNumber)
}

@Composable
fun StoreOrgContentScreen(personalNumber: String) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Magenta)) {
        Text(
            text = "StoreOrgScreen for $personalNumber",
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .padding(top = 100.dp)
        )
    }

}

@Preview
@Composable
fun StoreOrgScreenPreview() {
    StoreOrgContentScreen("123456789")
}