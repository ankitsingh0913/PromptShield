package com.xclone.detector_engine.scoring

import com.xclone.detector_engine.models.DetectionResult
import com.xclone.detector_engine.models.SensitiveType

class RiskScorer {

    fun calculatScore(
        result: List<DetectionResult>
    ): Int{
        var score = 0
        result.forEach {
            score += when(it.type) {
                SensitiveType.EMAIL -> 10
                SensitiveType.PHONE -> 15
                SensitiveType.API_KEY -> 40
                SensitiveType.PASSWORD -> 50
                SensitiveType.CODE_BLOCK -> 20
            }
        }
        return score.coerceIn(0,100)
    }
}