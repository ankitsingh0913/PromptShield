package com.xclone.feature_scan.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.xclone.feature_scan.presentation.viewmodel.ScanViewModel

@Composable
fun ScanScreen(
    viewModel: ScanViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = uiState.input,
            onValueChange = {
                viewModel.updateInput(it)
            },
            label = {
                Text("Paste Prompt")
            }
        )
        Text(
            text = "Risk Score: ${uiState.riskScore}"
        )
    }
}