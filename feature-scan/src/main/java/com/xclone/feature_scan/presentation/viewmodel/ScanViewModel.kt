package com.xclone.feature_scan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xclone.detector_engine.masking.MaskingEngine
import com.xclone.detector_engine.scanner.SensitiveDataScanner
import com.xclone.detector_engine.scoring.RiskScorer
import com.xclone.domain.model.PromptHistory
import com.xclone.domain.repository.PromptHistoryRepository
import com.xclone.feature_scan.presentation.state.ScanUiState
import com.xclone.feature_scan.presentation.utils.HighlightTextBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val repository: PromptHistoryRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(ScanUiState())

    private val scanner = SensitiveDataScanner()
    private val scorer = RiskScorer()
    private val masker = MaskingEngine()

    val uiState : StateFlow<ScanUiState> = _uiState.asStateFlow()

    fun updateInput(text : String){
        _uiState.value =
            _uiState.value.copy(
                input = text
            )
    }

    fun analyze(text: String) {

        val results =
            scanner.scan(text)

        val score =
            scorer.calculatScore(results)

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
}