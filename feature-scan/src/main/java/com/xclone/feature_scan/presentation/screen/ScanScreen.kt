package com.xclone.feature_scan.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.xclone.feature_scan.presentation.viewmodel.ScanViewModel

@Composable
fun ScanScreen(
    sharedText: String?,
    viewModel: ScanViewModel = hiltViewModel()
) {

    val uiState by
    viewModel.uiState.collectAsState()

    LaunchedEffect(
        sharedText
    ) {
        sharedText?.let {
            viewModel.analyze(it)
        }
    }

    val riskColor =
        getRiskColor(uiState.riskScore)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = uiState.input,
            onValueChange = {
                viewModel.analyze(it)
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Paste prompt")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text =
                        "Profile: ${
                            uiState.activeProfile.name
                        }"
                )
                Text(
                    text =
                        "Risk Score: ${uiState.riskScore}",
                    color = riskColor,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                LinearProgressIndicator(
                    progress = {
                        uiState.riskScore / 100f
                    },
                    modifier = Modifier.fillMaxWidth(),
                    color = riskColor
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text =
                        "Level: ${
                            getRiskLevel(
                                uiState.riskScore
                            )
                        }"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Highlighted Prompt",
            style =
                MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card {

            Text(
                text = uiState.highlightedText,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Findings",
            style =
                MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {

            items(uiState.findings) { finding ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {

                    Column(
                        modifier =
                            Modifier.padding(12.dp)
                    ) {

                        Text(
                            text =
                                finding.type.name,
                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            text =
                                finding.matchedText
                        )

                        Text(
                            text =
                                "Severity: ${
                                    finding.severity
                                }"
                        )

                        Text(
                            text =
                                "Confidence: ${
                                    (finding.confidence * 100)
                                        .toInt()
                                }%"
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Cleaned Prompt",
            style =
                MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card {

            Text(
                text = uiState.cleanedText,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
fun getRiskLevel(
    score: Int
): String {

    return when {

        score <= 20 -> "LOW"

        score <= 50 -> "MEDIUM"

        score <= 80 -> "HIGH"

        else -> "CRITICAL"
    }
}

fun getRiskColor(
    score: Int
): Color {

    return when {

        score <= 20 -> Color.Green

        score <= 50 -> Color(0xFFFFA000)

        score <= 80 -> Color.Red

        else -> Color.Magenta
    }
}