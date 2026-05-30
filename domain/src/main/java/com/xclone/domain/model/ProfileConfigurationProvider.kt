package com.xclone.domain.model

object ProfileConfigurationProvider {

    fun get(
        profile: WorkProfile
    ): ProfileConfiguration {

        return when(profile) {

            WorkProfile.DEVELOPER ->

                ProfileConfiguration(
                    emailWeight = 10,
                    phoneWeight = 5,
                    apiKeyWeight = 50,
                    passwordWeight = 50,
                    codeBlockWeight = 40
                )

            WorkProfile.STUDENT ->

                ProfileConfiguration(
                    emailWeight = 20,
                    phoneWeight = 20,
                    apiKeyWeight = 20,
                    passwordWeight = 30,
                    codeBlockWeight = 15
                )

            WorkProfile.RECRUITER ->

                ProfileConfiguration(
                    emailWeight = 30,
                    phoneWeight = 25,
                    apiKeyWeight = 10,
                    passwordWeight = 20,
                    codeBlockWeight = 5
                )

            WorkProfile.LEGAL ->

                ProfileConfiguration(
                    emailWeight = 15,
                    phoneWeight = 15,
                    apiKeyWeight = 10,
                    passwordWeight = 20,
                    codeBlockWeight = 5
                )
        }
    }
}