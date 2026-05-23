package com.xclone.feature_scan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.xclone.detector_engine.masking.MaskingEngine
import com.xclone.detector_engine.scanner.SensitiveDataScanner
import com.xclone.detector_engine.scoring.RiskScorer
import com.xclone.feature_scan.presentation.state.ScanUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(): ViewModel() {

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

        _uiState.value =
            _uiState.value.copy(
                input = text,
                findings = results.map { it.type.name },
                riskScore = score
            )
    }
}