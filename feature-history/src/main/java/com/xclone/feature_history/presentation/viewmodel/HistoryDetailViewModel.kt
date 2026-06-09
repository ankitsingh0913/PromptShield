package com.xclone.feature_history.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xclone.domain.repository.PromptHistoryRepository
import com.xclone.feature_history.presentation.state.HistoryDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HistoryDetailViewModel @Inject constructor(
    repository: PromptHistoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val historyId: Long =
        savedStateHandle["historyId"] ?: -1L

    val uiState: StateFlow<HistoryDetailUiState> =
        repository
            .getPromptById(historyId)
            .map { prompt ->

                if (prompt == null) {
                    HistoryDetailUiState(
                        isLoading = false,
                        prompt = null,
                        errorMessage = "History item not found"
                    )
                } else {
                    HistoryDetailUiState(
                        isLoading = false,
                        prompt = prompt,
                        errorMessage = null
                    )
                }
            }
            .catch { exception ->

                emit(
                    HistoryDetailUiState(
                        isLoading = false,
                        prompt = null,
                        errorMessage =
                            exception.localizedMessage
                                ?: "Failed to load history item"
                    )
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = HistoryDetailUiState()
            )
}