package ru.kettuproj.cloudalbum.model

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Album(
    @Required
    @SerialName("id") val id: Int,
    @Required
    @SerialName("creatorId") val creatorId: String,
    @Required
    @SerialName("name") val name: String
)
