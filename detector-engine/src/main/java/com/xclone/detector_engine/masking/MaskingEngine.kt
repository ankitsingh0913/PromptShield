package com.xclone.detector_engine.masking

import com.xclone.detector_engine.models.DetectionResult
import com.xclone.detector_engine.models.SensitiveType

class MaskingEngine {

    fun mask(
        text: String,
        result: List<DetectionResult>
    ): String {
        var maskedText = text
        result.forEach {
            val replacement = when(it.type) {
                SensitiveType.EMAIL -> "[EMAIL]"
                SensitiveType.PHONE -> "[PHONE]"
                SensitiveType.API_KEY -> "[API_KEY]"
                SensitiveType.PASSWORD -> "[PASSWORD]"
                SensitiveType.CODE_BLOCK -> "[CODE_BLOCK]"
            }
            maskedText = maskedText.replace(it.matchedText, replacement)
        }
        return maskedText
    }
}