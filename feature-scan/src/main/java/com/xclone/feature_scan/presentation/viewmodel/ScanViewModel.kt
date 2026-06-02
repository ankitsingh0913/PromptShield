package com.xclone.feature_scan.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xclone.detector_engine.masking.MaskingEngine
import com.xclone.detector_engine.scanner.SensitiveDataScanner
import com.xclone.detector_engine.scoring.RiskScorer
import com.xclone.domain.model.DetectionProfileProvider
import com.xclone.domain.model.ProfileConfigurationProvider
import com.xclone.domain.model.PromptHistory
import com.xclone.domain.model.WorkProfile
import com.xclone.domain.repository.AiRewriteRepository
import com.xclone.domain.repository.PromptHistoryRepository
import com.xclone.domain.repository.SettingsRepository
import com.xclone.feature_scan.presentation.state.ScanUiState
import com.xclone.feature_scan.presentation.utils.HighlightTextBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val repository: PromptHistoryRepository,
    private val settingsRepository: SettingsRepository,
    private val aiRepository: AiRewriteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScanUiState())
    val uiState: StateFlow<ScanUiState> = _uiState.asStateFlow()

    private val scanner = SensitiveDataScanner()
    private val scorer = RiskScorer()
    private val masker = MaskingEngine()
    private var currentProfile = WorkProfile.DEVELOPER
    private var autoSaveEnabled = true

    // NEW: Job for debouncing analysis
    private var analysisJob: Job? = null

    init {
        // Collect profile changes
        settingsRepository.selectedProfile
            .onEach {
                currentProfile = it
                _uiState.value = _uiState.value.copy(activeProfile = it)
            }
            .launchIn(viewModelScope)

        // NEW: Collect auto-save preference
        settingsRepository.autoSaveEnabled
            .onEach { enabled ->
                autoSaveEnabled = enabled
            }
            .launchIn(viewModelScope)
    }

    // NEW: Clear error message
    fun dismissError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    // NEW: Debounced text change (for user typing)
    fun onTextChanged(text: String) {
        // Update UI immediately
        _uiState.value = _uiState.value.copy(
            input = text,
            errorMessage = null // Clear error on new input
        )

        // Cancel previous analysis
        analysisJob?.cancel()

        // Debounce analysis
        analysisJob = viewModelScope.launch {
            if (text.isBlank()) {
                // Reset state if empty
                _uiState.value = _uiState.value.copy(
                    riskScore = 0,
                    findings = emptyList(),
                    cleanedText = "",
                    highlightedText = androidx.compose.ui.text.AnnotatedString("")
                )
                return@launch
            }

            delay(300) // 300ms debounce
            performAnalysis(text)
        }
    }

    // NEW: Immediate analysis (for shared text from other apps)
    fun analyzeImmediately(text: String) {
        analysisJob?.cancel()
        _uiState.value = _uiState.value.copy(
            input = text,
            errorMessage = null
        )
        viewModelScope.launch {
            performAnalysis(text)
        }
    }

    // NEW: Private method containing actual analysis logic
    private suspend fun performAnalysis(text: String) {
        try {
            val detectionProfile = DetectionProfileProvider.get(currentProfile)
            val results = scanner.scan(text, detectionProfile)
            val configuration = ProfileConfigurationProvider.get(currentProfile)
            val score = scorer.calculateScore(results, configuration)
            val cleaned = masker.mask(text, results)
            val highlighted = HighlightTextBuilder().build(text, results)

            _uiState.value = _uiState.value.copy(
                findings = results,
                riskScore = score,
                cleanedText = cleaned,
                highlightedText = highlighted
            )

            // NEW: Only save to history if auto-save is enabled
            if (autoSaveEnabled && text.isNotBlank()) {
                repository.insertPrompt(
                    PromptHistory(
                        originalText = text,
                        cleanedText = cleaned,
                        riskScore = score,
                        timestamp = System.currentTimeMillis()
                    )
                )
            }
        } catch (e: Exception) {
            Log.e("SCAN_ANALYSIS", "Error analyzing text", e)
            _uiState.value = _uiState.value.copy(
                errorMessage = "Analysis failed: ${e.localizedMessage ?: "Unknown error"}"
            )
        }
    }

    // DEPRECATED: Kept for compatibility but redirects to debounced version
    // Remove this if you're only using the new methods above
    fun analyze(text: String) {
        onTextChanged(text)
    }

    fun generateSuggestion() {
        if (_uiState.value.input.isBlank()) return

        _uiState.value = _uiState.value.copy(isGeneratingSuggestion = true)

        viewModelScope.launch {
            try {
                val suggestion = aiRepository.rewritePrompt(
                    _uiState.value.input,
                    _uiState.value.findings.map { it.type.name },
                    currentProfile.name
                )

                _uiState.value = _uiState.value.copy(
                    aiSuggestion = suggestion,
                    isGeneratingSuggestion = false
                )
            } catch (e: Exception) {
                Log.e("AI_REWRITE", "Error", e)
                _uiState.value = _uiState.value.copy(
                    isGeneratingSuggestion = false,
                    errorMessage = "AI generation failed: ${e.localizedMessage ?: "Unknown error"}"
                )
            }
        }
    }
}