package com.xclone.domain.model

data class ProfileConfiguration(
    val emailWeight: Int,

    val phoneWeight: Int,

    val apiKeyWeight: Int,

    val passwordWeight: Int,

    val codeBlockWeight: Int
)
