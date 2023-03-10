package ru.kettuproj.cloudalbum.ui.component.image

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import ru.kettuproj.cloudalbum.common.Constant
import ru.kettuproj.cloudalbum.common.requestImage
import ru.kettuproj.cloudalbum.model.Image
import ru.kettuproj.cloudalbum.ui.component.animation.Shimmer

@Composable
fun AlbumImage(
    onClick: () -> Unit,
    image: Image,
    token: String? = null
){
    val loading = remember { mutableStateOf(true) }
    val request = requestImage(image, token)
    val modifier = Modifier
        .padding(1.dp)
        .height(128.dp)
        .width(128.dp)
    Box(
        modifier = modifier
            .clickable { onClick() }
    ){
        if(loading.value) Shimmer(modifier)
        AsyncImage(
            model = request,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            onSuccess = {
                loading.value = false
            }
        )
    }
}