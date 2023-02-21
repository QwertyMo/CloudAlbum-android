package ru.kettuproj.cloudalbum.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import ru.kettuproj.cloudalbum.common.Constant
import ru.kettuproj.cloudalbum.model.Image

@Composable
fun AlbumImage(
    onClick: () -> Unit,
    image: Image
){
    val cacheId = "kettu_album_${image.uuid}_${image.created}"

    val request = ImageRequest.Builder(LocalContext.current)
        .data(Constant.imageURL(image.uuid))
        .memoryCacheKey(cacheId)
        .diskCacheKey(cacheId)
        .networkCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()

    Box(
        modifier = Modifier
            .padding(1.dp)
            .height(128.dp)
            .width(128.dp)
            .clickable { onClick() }
    ){
        AsyncImage(
            contentScale = ContentScale.Crop,
            model = request,
            contentDescription = ""
        )
    }
}