package ru.kettuproj.cloudalbum.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil.request.CachePolicy
import coil.request.ImageRequest
import ru.kettuproj.cloudalbum.model.Image

@Composable
fun requestImage(image: Image, token: String? = null):ImageRequest{
    val cacheId = Constant.cacheID(image)

    val request = ImageRequest.Builder(LocalContext.current)
    if(token!=null) request
        .addHeader("Authorization", "Bearer $token")
    return request.data(Constant.imageURL(image.uuid))
        .memoryCacheKey(cacheId)
        .diskCacheKey(cacheId)
        .networkCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()
}