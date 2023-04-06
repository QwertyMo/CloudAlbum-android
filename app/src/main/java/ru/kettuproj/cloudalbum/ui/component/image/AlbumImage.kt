package ru.kettuproj.cloudalbum.ui.component.image

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.kettuproj.cloudalbum.common.requestImage
import ru.kettuproj.cloudalbum.model.Image
import ru.kettuproj.cloudalbum.ui.component.animation.Shimmer

@Composable
fun AlbumImage(
    color: Color = Color.Transparent,
    onClick: () -> Unit = {},
    clickable: Boolean = true,
    image: Image,
    token: String? = null,
    shimmered: Boolean = true,
    size: Int = 128,
    padding: Int = 1
){
    val loading = remember { mutableStateOf(true) }
    val request = requestImage(image, token)
    var modifier = Modifier
        .padding(padding.dp)
        .height(size.dp)
        .width(size.dp)
    if(clickable) modifier = modifier.clickable{ onClick() }
    Box(
        modifier = modifier
    ){
        if(loading.value && shimmered) Shimmer(modifier)
        AsyncImage(
            model = request,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            onSuccess = {
                loading.value = false
            }
        )
        Box(modifier = Modifier.background(color).fillMaxSize())
    }
}