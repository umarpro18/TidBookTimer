package com.sample.tidbooktimer.tidbookhome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sample.tidbooktimer.R

@Composable
fun SelectOrgScreen(orgNoList: List<String>, onOrgSelected: (String) -> Unit) {
    // Start here
    if (orgNoList.isNotEmpty()) {
        SelectOrgContentScreen(orgNoList, onOrgSelected)
    } else {
        EmptyOrgListScreen()
    }
}

@Composable
fun SelectOrgContentScreen(orgNoList: List<String>, onOrgSelected: (String) -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.ic_background),
            modifier = Modifier.fillMaxSize(),
            contentDescription = null
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.selectOrg),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Magenta
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 60.dp)
            ) {
                items(orgNoList) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {
                        Button(
                            onClick = { onOrgSelected(it) }, modifier = Modifier
                                .fillMaxWidth()
                                .size(60.dp)
                        ) {

                            Text(text = it, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyOrgListScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.ic_background),
            modifier = Modifier.fillMaxSize(), contentDescription = null
        )

        Text(
            text = "No org found, Please add org to continue!",
            fontSize = 24.sp,
            color = Color.DarkGray,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(start = 32.dp, end = 32.dp)
        )
    }
}

@Preview
@Composable
fun SelectOrgScreenPreview() {
    SelectOrgContentScreen(listOf("122", "123", "124"), onOrgSelected = {})
}

@Preview
@Composable
fun EmptyOrgListScreenPreview() {
    EmptyOrgListScreen()
}