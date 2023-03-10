package ru.kettuproj.cloudalbum.ui.component.grid

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.kettuproj.cloudalbum.model.Image
import ru.kettuproj.cloudalbum.screen.Destination
import ru.kettuproj.cloudalbum.screen.navigate
import ru.kettuproj.cloudalbum.ui.component.image.AlbumImage

@Composable
fun ImageGrid(navController: NavController, token: String?, images: List<Image>){
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        modifier = Modifier.fillMaxHeight()){
        items(items = images, itemContent = {
            AlbumImage(
                onClick = { navController.navigate(Destination.IMAGE, it.uuid) },
                image = it,
                token = token
            )
        })
    }
}