package ru.kettuproj.cloudalbum.model

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Token(
    @Required
    @SerialName("token") val token: String
)