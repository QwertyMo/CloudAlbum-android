package ru.kettuproj.cloudalbum.model

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Image(
    @Required
    @SerialName("userId") val userId: Int,
    @Required
    @SerialName("uuid") val uuid: String,
    @Required
    @SerialName("created") val created: Long
)