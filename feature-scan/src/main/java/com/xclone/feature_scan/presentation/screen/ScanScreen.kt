package com.xclone.feature_scan.presentation.screen

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.xclone.feature_scan.presentation.viewmodel.ScanViewModel
import kotlinx.coroutines.launch

@Composable
fun ScanScreen(
    sharedText: String? = null,
    viewModel: ScanViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(sharedText) {
        sharedText?.let {
            viewModel.analyzeImmediately(it)
        }
    }

    val riskColor = getRiskColor(uiState.riskScore)

    // NEW: Determine if copy button should be enabled
    val canCopy = uiState.aiSuggestion.isNotBlank()
            && !uiState.isGeneratingSuggestion
            && !uiState.aiSuggestion.startsWith("Failed")
            && !uiState.aiSuggestion.startsWith("Unable")

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            // Error Banner
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
                            TextButton(
                                onClick = { viewModel.dismissError() }
                            ) {
                                Text("Dismiss")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Input Section
            item {
                OutlinedTextField(
                    value = uiState.input,
                    onValueChange = { text ->
                        viewModel.onTextChanged(text)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Paste Prompt") },
                    placeholder = { Text("Enter text to scan for sensitive data...") }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Risk Card
            item {
                Card {
                    Column(modifier = Modifier.padding(16.dp)) {
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

                        // Divider
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        // Local Explanation (Instant)
                        Text(
                            text = uiState.localExplanation,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 3
                        )

                        // AI Explanation Button (Only show if findings exist)
                        if (uiState.findings.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { viewModel.generateAiExplanation() },
                                enabled = !uiState.isGeneratingAiExplanation,
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                if (uiState.isGeneratingAiExplanation) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                } else {
                                    Icon(
                                        Icons.Outlined.Lightbulb,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Get AI Risk Explanation", fontSize = 12.sp)
                                }
                            }
                        }

                        // AI Explanation Result
                        if (uiState.aiExplanation.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Text(
                                    text = uiState.aiExplanation,
                                    modifier = Modifier.padding(12.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Highlighted Prompt
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

            // Findings Header
            item {
                Text(
                    text = "Findings",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Findings List
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
                        Text(
                            text = "Confidence: ${(finding.confidence * 100).toInt()}%"
                        )
                    }
                }
            }

            // Cleaned Prompt
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

            // AI Rewrite Section
            item {
                Button(
                    onClick = { viewModel.generateSuggestion() },
                    enabled = !uiState.isGeneratingSuggestion
                            && uiState.input.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (uiState.isGeneratingSuggestion) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Generating...")
                    } else {
                        Text("Generate AI Rewrite")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // NEW: Header with Copy Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "AI Rewrite Suggestion",
                        style = MaterialTheme.typography.titleMedium
                    )

                    // NEW: Copy Button
                    IconButton(
                        onClick = {
                            val clipboard = context.getSystemService(
                                Context.CLIPBOARD_SERVICE
                            ) as ClipboardManager

                            val clip = ClipData.newPlainText(
                                "AI Rewrite",
                                uiState.aiSuggestion
                            )
                            clipboard.setPrimaryClip(clip)

                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Copied to clipboard",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        },
                        enabled = canCopy
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy AI suggestion"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (uiState.isGeneratingSuggestion) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (uiState.aiSuggestion.isNotBlank()) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme
                                .colorScheme
                                .secondaryContainer
                        )
                    ) {
                        Text(
                            text = uiState.aiSuggestion,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme
                                .colorScheme
                                .surfaceVariant
                        )
                    ) {
                        Text(
                            text = "Press the button above to generate an AI-powered safe rewrite of your prompt.",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

fun getRiskLevel(score: Int): String {
    return when {
        score <= 20 -> "LOW"
        score <= 50 -> "MEDIUM"
        score <= 80 -> "HIGH"
        else -> "CRITICAL"
    }
}

fun getRiskColor(score: Int): Color {
    return when {
        score <= 20 -> Color.Green
        score <= 50 -> Color(0xFFFFA000)
        score <= 80 -> Color.Red
        else -> Color.Magenta
    }
}