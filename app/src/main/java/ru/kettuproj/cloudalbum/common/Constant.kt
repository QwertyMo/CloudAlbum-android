package ru.kettuproj.cloudalbum.common

object Constant {
    const val REST_HOST         = "http://188.187.60.84:5232"

    fun imageURL(uuid: String): String = "$REST_HOST/image/data/$uuid"
}