package ru.kettuproj.cloudalbum.ui.component.image

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.kettuproj.cloudalbum.common.dp
import ru.kettuproj.cloudalbum.common.requestImage
import ru.kettuproj.cloudalbum.model.Album
import ru.kettuproj.cloudalbum.model.Image
import ru.kettuproj.cloudalbum.model.User
import ru.kettuproj.cloudalbum.ui.RandomGradient
import ru.kettuproj.cloudalbum.ui.component.animation.Shimmer
import kotlin.random.Random

@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    size: Int = 64,
    user: User? = null,
    album: Album? = null,
    image: Image? = null,
    token: String? = null){

    val mod = modifier
        .size(size.dp)
        .clip(CircleShape)

    if(user == null && album == null){
        Shimmer(modifier = mod)
    }
    else if(image == null){
        var id = 0
        var text = ""
        if(user!=null) {
            text = user.login[0].toString()
            id = user.id
        }
        else if(album!=null) {
            text = album.name[0].toString()
            id = album.id
        }
        val grad = RandomGradient.generate(id)
        Box(modifier = mod
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        grad.first,
                        grad.second
                    )
                )
            ),
            contentAlignment = Alignment.Center
        ){

            Text(
                text,
                fontSize = (size/2).dp(),
                color = Color.White
            )
        }
    }
    else{
        val loading = remember { mutableStateOf(true) }
        Box(modifier = mod){
            val request = requestImage(image, token)
            if(loading.value) Shimmer(mod)
            AsyncImage(
                contentScale = ContentScale.Crop,
                model = request,
                contentDescription = null,
                onSuccess = {
                    loading.value = false
                }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewProfileImage(){
    Row{
        ProfileImage()
        ProfileImage(user = User(6,"QwertyMo", 0))
        ProfileImage(user = User(2,"Test", 0))
        ProfileImage(user = User(0, "", 0), image = Image(0, "3f1064f0-d903-4341-a01a-0f6f7b6f680d", 0))
    }
}