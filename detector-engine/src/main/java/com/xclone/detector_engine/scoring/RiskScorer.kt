package com.xclone.detector_engine.scoring

import com.xclone.detector_engine.models.DetectionResult
import com.xclone.detector_engine.models.SensitiveType
import com.xclone.domain.model.ProfileConfiguration

class RiskScorer {

    fun calculateScore(
        results: List<DetectionResult>,
        configuration: ProfileConfiguration
    ): Int {

        var score = 0

        results.forEach {

            score += when(it.type) {

                SensitiveType.EMAIL ->
                    configuration.emailWeight

                SensitiveType.PHONE ->
                    configuration.phoneWeight

                SensitiveType.API_KEY ->
                    configuration.apiKeyWeight

                SensitiveType.PASSWORD ->
                    configuration.passwordWeight

                SensitiveType.CODE_BLOCK ->
                    configuration.codeBlockWeight
            }
        }

        return score.coerceIn(0,100)
    }
}