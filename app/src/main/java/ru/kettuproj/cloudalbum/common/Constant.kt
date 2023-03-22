package ru.kettuproj.cloudalbum.common

import ru.kettuproj.cloudalbum.model.Image

object Constant {
    const val REST_HOST         = "http://188.187.60.84:5232"

    fun imageURL(uuid: String): String = "$REST_HOST/image/data/$uuid"

    fun cacheID(image: Image): String = "kettu_album_${image.uuid}_${image.created}"
}