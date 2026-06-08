package com.xclone.feature_scan.presentation.state

import androidx.compose.ui.text.AnnotatedString
import com.xclone.detector_engine.models.DetectionResult
import com.xclone.domain.model.WorkProfile

data class ScanUiState(
    val input: String = "",
    val riskScore: Int = 0,
    val findings: List<DetectionResult> = emptyList(),
    val cleanedText: String = "",
    val isScanning: Boolean = false,
    val highlightedText: AnnotatedString = AnnotatedString(""),
    val activeProfile: WorkProfile = WorkProfile.DEVELOPER,
    val aiSuggestion: String = "",
    val isGeneratingSuggestion: Boolean = false,
    val errorMessage: String? = null,  // NEW: For error handling

    val localExplanation: String = "",
    val aiExplanation: String = "",
    val isGeneratingAiExplanation: Boolean = false
)