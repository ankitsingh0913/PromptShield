package com.xclone.detector_engine.models

data class DetectionResult(
    val type: SensitiveType,
    val matchedText: String,
    val startIndex: Int,
    val endIndex: Int,
    val severity: Severity,
    val confidence: Float
)
