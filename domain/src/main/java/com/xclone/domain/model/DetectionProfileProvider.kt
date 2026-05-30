package com.xclone.domain.model

object DetectionProfileProvider {

    fun get(
        profile: WorkProfile
    ): DetectionProfile {

        return when(profile) {

            WorkProfile.DEVELOPER ->

                DetectionProfile(
                    detectEmails = true,
                    detectPhones = false,
                    detectApiKeys = true,
                    detectPasswords = true,
                    detectCodeBlocks = true
                )

            WorkProfile.STUDENT ->

                DetectionProfile(
                    detectEmails = true,
                    detectPhones = true,
                    detectApiKeys = true,
                    detectPasswords = true,
                    detectCodeBlocks = true
                )

            WorkProfile.RECRUITER ->

                DetectionProfile(
                    detectEmails = true,
                    detectPhones = true,
                    detectApiKeys = false,
                    detectPasswords = false,
                    detectCodeBlocks = false
                )

            WorkProfile.LEGAL ->

                DetectionProfile(
                    detectEmails = true,
                    detectPhones = true,
                    detectApiKeys = false,
                    detectPasswords = false,
                    detectCodeBlocks = false
                )
        }
    }
}