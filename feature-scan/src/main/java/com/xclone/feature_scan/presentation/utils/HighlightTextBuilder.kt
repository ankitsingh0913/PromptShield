package com.xclone.feature_scan.presentation.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.xclone.detector_engine.models.DetectionResult
import com.xclone.detector_engine.models.Severity

class HighlightTextBuilder {

    fun build(
        text: String,
        findings: List<DetectionResult>
    ): AnnotatedString{
        return buildAnnotatedString {
            var currentIndex = 0
            val sortedFindings = findings.sortedBy { it.startIndex }
            sortedFindings.forEach { result ->
                if(currentIndex < result.startIndex){
                    append(
                        text.substring(
                            currentIndex,
                            result.startIndex
                        )
                    )
                }
                val color = when(result.severity){
                    Severity.LOW -> Color.Green
                    Severity.MEDIUM -> Color(0xFFFFA000)
                    Severity.HIGH -> Color.Red
                    Severity.CRITICAL -> Color.Magenta
                }
                withStyle(
                    style = SpanStyle(
                        background = color.copy(alpha = 0.3f)
                    )
                ) {
                    append(result.matchedText)
                }
                currentIndex = result.endIndex + 1
            }
            if(currentIndex < text.length){
                append(text.substring(currentIndex))
            }
        }
    }
}