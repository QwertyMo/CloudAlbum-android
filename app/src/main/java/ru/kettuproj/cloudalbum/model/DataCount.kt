package ru.kettuproj.cloudalbum.model

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class DataCount(
    @Required
    @SerialName("images") val images: Int,
)
