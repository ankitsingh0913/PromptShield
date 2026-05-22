package com.xclone.feature_scan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.xclone.feature_scan.presentation.state.ScanUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(): ViewModel() {

    private val _uiState = MutableStateFlow(ScanUiState())

    val uiState : StateFlow<ScanUiState> = _uiState.asStateFlow()

    fun updateInput(text : String){
        _uiState.value =
            _uiState.value.copy(
                input = text
            )
    }
}