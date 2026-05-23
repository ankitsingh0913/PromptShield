package com.xclone.detector_engine.scanner

import com.xclone.detector_engine.models.DetectionResult
import com.xclone.detector_engine.models.SensitiveType
import com.xclone.detector_engine.models.Severity
import com.xclone.detector_engine.regex.RegexPatterns

class SensitiveDataScanner {
    fun scan(text: String): List<DetectionResult> {
        val results = mutableListOf<DetectionResult>()
        scanEmails(text,results)
        scanPhones(text, results)
        scanApiKeys(text, results)
        scanPasswords(text, results)
        scanCodeBlocks(text, results)
        return results
    }

    private fun scanEmails(
        text: String,
        results: MutableList<DetectionResult>
    ) {
        RegexPatterns.EMAIL.findAll(text).forEach {
            results.add(
                DetectionResult(
                    type = SensitiveType.EMAIL,
                    matchedText = it.value,
                    startIndex = it.range.first,
                    endIndex = it.range.last + 1,
                    severity = Severity.MEDIUM,
                    confidence = 0.95f
                )
            )
        }
    }

    fun scanPhones(
        text: String,
        results: MutableList<DetectionResult>
    ) {
        RegexPatterns.PHONE.findAll(text).forEach {
            results.add(
                DetectionResult(
                    type = SensitiveType.PHONE,
                    matchedText = it.value,
                    startIndex = it.range.first,
                    endIndex = it.range.last + 1,
                    severity = Severity.MEDIUM,
                    confidence = 0.9f
                )
            )
        }
    }

    fun scanApiKeys(
        text: String,
        results: MutableList<DetectionResult>
    ) {
        RegexPatterns.OPENAI_KEY.findAll(text).forEach {
            results.add(
                DetectionResult(
                    type = SensitiveType.API_KEY,
                    matchedText = it.value,
                    startIndex = it.range.first,
                    endIndex = it.range.last + 1,
                    severity = Severity.HIGH,
                    confidence = 0.99f
                )
            )
        }
    }

    fun scanPasswords(
        text: String,
        results: MutableList<DetectionResult>
    ) {
        RegexPatterns.PASSWORD.findAll(text).forEach {
            results.add(
                DetectionResult(
                    type = SensitiveType.PASSWORD,
                    matchedText = it.value,
                    startIndex = it.range.first,
                    endIndex = it.range.last + 1,
                    severity = Severity.HIGH,
                    confidence = 0.85f
                )
            )
        }
    }

    fun scanCodeBlocks(
        text: String,
        results: MutableList<DetectionResult>
    ) {
        RegexPatterns.CODE_BLOCK.findAll(text).forEach {
            results.add(
                DetectionResult(
                    type = SensitiveType.CODE_BLOCK,
                    matchedText = it.value,
                    startIndex = it.range.first,
                    endIndex = it.range.last + 1,
                    severity = Severity.LOW,
                    confidence = 0.8f
                )
            )
        }
    }
}