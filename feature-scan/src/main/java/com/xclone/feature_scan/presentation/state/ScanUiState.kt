package com.xclone.feature_scan.presentation.state

data class ScanUiState (
    val input: String = "",

    val riskScore: Int = 0,

    val findings: List<String> = emptyList(),

    val isScanning: Boolean = false
)