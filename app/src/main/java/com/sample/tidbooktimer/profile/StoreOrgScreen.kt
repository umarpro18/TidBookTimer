package com.sample.tidbooktimer.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sample.tidbooktimer.R

@Composable
fun StoreOrgScreen(
    viewModel: StoreOrgViewModel,
    personalNumber: String,
    onStoreOrgSuccess: () -> Unit,
) {
    val orgNo1 = viewModel.orgNo1.collectAsStateWithLifecycle()
    val orgNo2 = viewModel.orgNo2.collectAsStateWithLifecycle()
    val orgNo3 = viewModel.orgNo3.collectAsStateWithLifecycle()
    val orgNo4 = viewModel.orgNo4.collectAsStateWithLifecycle()

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    val orgNo1Value: (String) -> Unit = { viewModel.setOrgNo1(it) }
    val orgNo2Value: (String) -> Unit = { viewModel.setOrgNo2(it) }
    val orgNo3Value: (String) -> Unit = { viewModel.setOrgNo3(it) }
    val orgNo4Value: (String) -> Unit = { viewModel.setOrgNo4(it) }

    val context = LocalContext.current

    val isLoading = uiState.value is StoreOrgDetailState.Loading

    val onStoreOrgDetailClicked: (String, String?, String?, String?, String?) -> Unit =
        { personalNumber, orgNo1, orgNo2, orgNo3, orgNo4 ->
            viewModel.storeOrgDetail(personalNumber, orgNo1, orgNo2, orgNo3, orgNo4)
        }

    LaunchedEffect(uiState.value) {
        when (uiState.value) {
            is StoreOrgDetailState.Success -> onStoreOrgSuccess()
            is StoreOrgDetailState.Error -> {
                android.widget.Toast.makeText(
                    context,
                    (uiState.value as StoreOrgDetailState.Error).message,
                    android.widget.Toast.LENGTH_LONG
                ).show()
            }

            else -> {}
        }
    }

    StoreOrgContentScreen(
        personalNumber,
        onStoreOrgDetailClicked,
        orgNo1.value,
        orgNo2.value,
        orgNo3.value,
        orgNo4.value,
        orgNo1Value,
        orgNo2Value,
        orgNo3Value,
        orgNo4Value,
        isLoading
    )
}

@Composable
fun StoreOrgContentScreen(
    personalNumber: String,
    onStoreOrgDetailClicked: (String, String?, String?, String?, String?) -> Unit,
    orgNo1: String,
    orgNo2: String,
    orgNo3: String,
    orgNo4: String,
    orgNo1Value: (String) -> Unit,
    orgNo2Value: (String) -> Unit,
    orgNo3Value: (String) -> Unit,
    orgNo4Value: (String) -> Unit,
    isLoading: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Magenta)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_background),
            contentDescription = "Logo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Text(
            text = "Personal#:  $personalNumber",
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .padding(top = 24.dp, start = 16.dp, end = 16.dp),
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = Color.Black
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Store Organizations",
                modifier = Modifier
                    .padding(top = 20.dp, start = 16.dp, end = 16.dp),
                fontWeight = FontWeight.SemiBold,
                fontSize = 32.sp,
                color = Color.Magenta
            )

            Spacer(modifier = Modifier.size(32.dp))

            OutlinedTextField(
                value = orgNo1,
                onValueChange = { orgNo1Value(it) },
                label = { Text("Org #1*") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.size(32.dp))

            OutlinedTextField(
                value = orgNo2,
                onValueChange = { orgNo2Value(it) },
                label = { Text("Org #2") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.size(32.dp))

            OutlinedTextField(
                value = orgNo3,
                onValueChange = { orgNo3Value(it) },
                label = { Text("Org #3") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.size(32.dp))

            OutlinedTextField(
                value = orgNo4,
                onValueChange = { orgNo4Value(it) },
                label = { Text("Org #4") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.size(40.dp))

            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.Magenta
                )
            } else {
                Button(
                    onClick = {
                        onStoreOrgDetailClicked(
                            personalNumber,
                            orgNo1,
                            orgNo2,
                            orgNo3,
                            orgNo4
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color.Magenta.copy(alpha = 0.85f),
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Save & Continue", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }

            }
        }
    }
}

@Preview
@Composable
fun StoreOrgScreenPreview() {
    StoreOrgContentScreen(
        "123456789",
        onStoreOrgDetailClicked = { _, _, _, _, _ -> },
        "",
        "",
        "",
        "",
        orgNo1Value = {},
        orgNo2Value = {},
        orgNo3Value = {},
        orgNo4Value = {},
        false
    )
}
