package com.xclone.feature_history.presentation.state

import com.xclone.domain.model.PromptHistory

data class HistoryDetailUiState(
    val isLoading: Boolean = true,
    val prompt: PromptHistory? = null,
    val errorMessage: String? = null
)