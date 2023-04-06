package ru.kettuproj.cloudalbum.ui.component.grid

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.kettuproj.cloudalbum.common.setScrolling
import ru.kettuproj.cloudalbum.model.Image
import ru.kettuproj.cloudalbum.screen.Destination
import ru.kettuproj.cloudalbum.screen.navigate
import ru.kettuproj.cloudalbum.ui.component.animation.Shimmer
import ru.kettuproj.cloudalbum.ui.component.image.AlbumImage

@Composable
fun ImageGrid(
    navController: NavController,
    token: String?,
    images: List<Image>,
    albumID: Int? = null,
    isLoaded: Boolean = true,
    loadCount: Int = 33
){

    if(!isLoaded){
        val state = rememberLazyGridState()
        LaunchedEffect(Unit){ state.setScrolling(false) }
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp),
            modifier = Modifier.fillMaxHeight(),
            state = state
            ){
            items(loadCount){
                Shimmer(
                    Modifier
                        .padding(1.dp)
                        .height(128.dp)
                        .width(128.dp))
            }
        }
    }
    else{
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp),
            modifier = Modifier.fillMaxHeight()){
            itemsIndexed(items = images){ index, item ->
                AlbumImage(
                    onClick = {
                        val args = mutableMapOf(Pair("pos", "$index"))
                        if(albumID!=null) args["album"] = "$albumID"
                        navController.navigate(Destination.IMAGE, args)
                    },
                    image = item,
                    token = token
                )
            }
        }
    }
}