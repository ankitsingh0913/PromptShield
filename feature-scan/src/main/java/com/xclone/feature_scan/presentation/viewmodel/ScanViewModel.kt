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
): ViewModel() {

    private val _uiState = MutableStateFlow(ScanUiState())

    private val scanner = SensitiveDataScanner()
    private val scorer = RiskScorer()
    private val masker = MaskingEngine()
    private var currentProfile = WorkProfile.DEVELOPER

    val uiState : StateFlow<ScanUiState> = _uiState.asStateFlow()

    fun updateInput(text : String){
        _uiState.value =
            _uiState.value.copy(
                input = text
            )
    }

    init {
        settingsRepository
            .selectedProfile
            .onEach {
                currentProfile = it
                _uiState.value =
                    _uiState.value.copy(
                        activeProfile = it
                    )
            }
            .launchIn(viewModelScope)
    }

    fun analyze(text: String) {

        val detectionProfile = DetectionProfileProvider.get(currentProfile)

        val results =
            scanner.scan(text,detectionProfile)

        val configuration =
            ProfileConfigurationProvider.get(
                currentProfile
            )

        val score =
            scorer.calculateScore(
                results,
                configuration
            )

        val cleaned =
            masker.mask(text, results)

        val highlighted = HighlightTextBuilder().build(
            text,
            results)

        _uiState.value =
            _uiState.value.copy(
                input = text,
                findings = results,
                riskScore = score,
                cleanedText = cleaned,
                highlightedText = highlighted
            )

        viewModelScope.launch {
            repository.insertPrompt(
                PromptHistory(
                    originalText = text,
                    cleanedText = cleaned,
                    riskScore = score,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    fun generateSuggestion() {

        _uiState.value =
            _uiState.value.copy(
                isGeneratingSuggestion = true
            )

        viewModelScope.launch {

            try {
                _uiState.value =
                    _uiState.value.copy(
                        isGeneratingSuggestion = true
                    )

                val suggestion =
                    aiRepository.rewritePrompt(
                        _uiState.value.input,
                        _uiState.value.findings.map {
                            it.type.name
                        }
                    )

                _uiState.value =
                    _uiState.value.copy(
                        aiSuggestion = suggestion,
                        isGeneratingSuggestion = false
                    )

            } catch (e: Exception) {

                Log.e(
                    "AI_REWRITE",
                    "Error",
                    e
                )

                _uiState.value =
                    _uiState.value.copy(
                        isGeneratingSuggestion = false,
                        aiSuggestion =
                            "Failed to generate suggestion"
                    )
            }
        }
    }
}