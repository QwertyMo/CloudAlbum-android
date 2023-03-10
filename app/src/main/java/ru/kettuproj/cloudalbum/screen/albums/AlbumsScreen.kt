package ru.kettuproj.cloudalbum.screen.albums

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.kettuproj.cloudalbum.model.Album
import ru.kettuproj.cloudalbum.screen.Destination
import ru.kettuproj.cloudalbum.screen.albums.viewmodel.AlbumsViewModel
import ru.kettuproj.cloudalbum.screen.navigate

@Composable
fun AlbumsScreen(navController: NavController){

    val context = LocalContext.current

    val viewModelImage: AlbumsViewModel = viewModel()

    val images = viewModelImage.albums as List<Album>

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Button(onClick = { navController.navigate(Destination.CREATE_ALBUM) }) {
            Text(text = "Create")
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()){
            items(items = images, itemContent = {
                AlbumElement(album = it, navController = navController)
            })
        }
    }

}

@Composable
fun AlbumElement(album: Album, navController: NavController){
    Box(
        modifier = Modifier
            .padding(1.dp)
            .height(128.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate(Destination.ALBUM, album.id.toString())
            }
    ){
        Card(modifier = Modifier.fillMaxSize()) {
            Row{
                Text(text = album.name)
            }
        }
    }
}
