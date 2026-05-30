package com.xclone.domain.model

data class DetectionProfile(

    val detectEmails: Boolean,

    val detectPhones: Boolean,

    val detectApiKeys: Boolean,

    val detectPasswords: Boolean,

    val detectCodeBlocks: Boolean
)