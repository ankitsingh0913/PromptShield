package com.xclone.feature_scan.presentation.utils

import com.xclone.detector_engine.models.DetectionResult
import com.xclone.detector_engine.models.Severity

object ExplanationGenerator {
    fun generateLocal(findings: List<DetectionResult>, riskScore: Int): String {
        if (findings.isEmpty()) return "No sensitive data detected. Your prompt appears safe to share."

        val types = findings.map { it.type.name.lowercase() }.distinct()
        val maxSeverity = findings.maxOfOrNull { it.severity } ?: Severity.LOW

        val severityText = when(maxSeverity) {
            Severity.LOW -> "low risk"
            Severity.MEDIUM -> "moderate risk"
            Severity.HIGH -> "high risk"
            Severity.CRITICAL -> "critical risk"
        }

        return "Detected ${findings.size} issue(s): ${types.joinToString(", ")}. " +
                "Overall assessment: $severityText ($riskScore/100). " +
                "Review highlighted items before sharing."
    }
}