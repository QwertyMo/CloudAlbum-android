package ru.kettuproj.cloudalbum.model

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @Required
    @SerialName("id") val id: Int,
    @Required
    @SerialName("login") val login: String,
    @Required
    @SerialName("created") val created: Long
)
