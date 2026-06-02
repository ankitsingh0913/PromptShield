package com.xclone.feature_scan.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.xclone.domain.model.WorkProfile
import com.xclone.feature_scan.presentation.state.ScanUiState
import com.xclone.feature_scan.presentation.viewmodel.ScanViewModel

@Composable
fun ScanScreen(
    sharedText: String? = null,
    viewModel: ScanViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    // UPDATED: Use analyzeImmediately for shared text (no debounce needed)
    LaunchedEffect(sharedText) {
        sharedText?.let {
            viewModel.analyzeImmediately(it)
        }
    }

    val riskColor = getRiskColor(uiState.riskScore)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // NEW: Error Banner
        item {
            if (uiState.errorMessage != null) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = uiState.errorMessage ?: "",
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        TextButton(onClick = { viewModel.dismissError() }) {
                            Text("Dismiss")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Input Section - UPDATED to use onTextChanged (debounced)
        item {
            OutlinedTextField(
                value = uiState.input,
                onValueChange = { text ->
                    viewModel.onTextChanged(text) // Debounced
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Paste Prompt") },
                placeholder = { Text("Enter text to scan for sensitive data...") }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Risk Card (existing code remains the same)
        item {
            Card {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Profile: ${uiState.activeProfile.name}")
                    Text(
                        text = "Risk Score: ${uiState.riskScore}",
                        color = riskColor,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { uiState.riskScore / 100f },
                        modifier = Modifier.fillMaxWidth(),
                        color = riskColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Level: ${getRiskLevel(uiState.riskScore)}")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Highlighted Prompt (existing)
        item {
            Text(
                text = "Highlighted Prompt",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card {
                Text(
                    text = uiState.highlightedText,
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Findings Header (existing)
        item {
            Text(
                text = "Findings",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Findings List (existing)
        items(uiState.findings) { finding ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = finding.type.name,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = finding.matchedText)
                    Text(text = "Severity: ${finding.severity}")
                    Text(text = "Confidence: ${(finding.confidence * 100).toInt()}%")
                }
            }
        }

        // Cleaned Prompt (existing)
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Cleaned Prompt",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card {
                Text(
                    text = uiState.cleanedText,
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // AI Rewrite Section (existing)
        item {
            Button(
                onClick = { viewModel.generateSuggestion() },
                enabled = !uiState.isGeneratingSuggestion && uiState.input.isNotBlank()
            ) {
                Text("Generate AI Rewrite")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "AI Rewrite Suggestion",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (uiState.isGeneratingSuggestion) {
                CircularProgressIndicator()
            } else {
                Card {
                    Text(
                        text = uiState.aiSuggestion.ifBlank { "Press button to generate AI suggestion" },
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(100.dp))
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