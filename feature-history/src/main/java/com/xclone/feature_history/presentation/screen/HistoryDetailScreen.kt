package com.xclone.feature_history.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.xclone.domain.model.PromptHistory
import com.xclone.feature_history.presentation.viewmodel.HistoryDetailViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDetailScreen(
    onBackClick: () -> Unit,
    viewModel: HistoryDetailViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("History Detail")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Text(
                            text = "←",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        when {

            uiState.isLoading -> {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.errorMessage != null -> {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.errorMessage ?: "Something went wrong",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            uiState.prompt != null -> {

                HistoryDetailContent(
                    prompt = uiState.prompt!!,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
private fun HistoryDetailContent(
    prompt: PromptHistory,
    modifier: Modifier = Modifier
) {

    val formattedTime = remember(prompt.timestamp) {
        formatTimestamp(prompt.timestamp)
    }

    val riskColor = getHistoryRiskColor(prompt.riskScore)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Risk Score",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "${prompt.riskScore}/100",
                        color = riskColor,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    LinearProgressIndicator(
                        progress = {
                            prompt.riskScore / 100f
                        },
                        modifier = Modifier.fillMaxWidth(),
                        color = riskColor
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "Level: ${getHistoryRiskLevel(prompt.riskScore)}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = formattedTime,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {

            HistoryTextSection(
                title = "Original Prompt",
                text = prompt.originalText
            )
        }

        item {

            HistoryTextSection(
                title = "Cleaned Prompt",
                text = prompt.cleanedText
            )
        }

        item {

            Card(
                colors = CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Note",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "This history item stores the original prompt, cleaned prompt, risk score, and timestamp. Exact old findings are not stored yet.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun HistoryTextSection(
    title: String,
    text: String
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            HorizontalDivider()

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            SelectionContainer {

                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

private fun getHistoryRiskLevel(score: Int): String {
    return when {
        score <= 20 -> "LOW"
        score <= 50 -> "MEDIUM"
        score <= 80 -> "HIGH"
        else -> "CRITICAL"
    }
}

private fun getHistoryRiskColor(score: Int): Color {
    return when {
        score <= 20 -> Color(0xFF2E7D32)
        score <= 50 -> Color(0xFFFFA000)
        score <= 80 -> Color(0xFFD32F2F)
        else -> Color(0xFF9C27B0)
    }
}

private fun formatTimestamp(timestamp: Long): String {
    return SimpleDateFormat(
        "dd MMM yyyy, hh:mm a",
        Locale.getDefault()
    ).format(Date(timestamp))
}