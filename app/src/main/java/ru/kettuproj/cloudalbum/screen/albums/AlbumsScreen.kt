package ru.kettuproj.cloudalbum.screen.albums

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.kettuproj.cloudalbum.R
import ru.kettuproj.cloudalbum.common.getStatusBarSize
import ru.kettuproj.cloudalbum.model.Album
import ru.kettuproj.cloudalbum.screen.Destination
import ru.kettuproj.cloudalbum.screen.albums.viewmodel.AlbumsViewModel
import ru.kettuproj.cloudalbum.screen.myProfile.logout
import ru.kettuproj.cloudalbum.screen.navigate
import ru.kettuproj.cloudalbum.ui.component.image.AlbumImage

@Composable
fun AlbumsScreen(navController: NavController, paddings: PaddingValues){

    val context = LocalContext.current

    val viewModelImage: AlbumsViewModel = viewModel()

    val images = viewModelImage.albums as List<Album>

    Box(modifier = Modifier
        .padding(
            PaddingValues(
                top = getStatusBarSize(LocalContext.current.resources)
            )
        )
        .padding(paddings)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp),
                modifier = Modifier.fillMaxHeight()){
                itemsIndexed(items = images){ index, item ->
                    AlbumElement(album = item, navController = navController)
                }
            }
        }
        ButtonCreateAlbum(onClick = {
            navController.navigate(Destination.CREATE_ALBUM)
        })
    }
}

@Composable
fun BoxScope.ButtonCreateAlbum(onClick: ()->Unit){
    FloatingActionButton(
        modifier = Modifier
            .padding(all = 16.dp)
            .align(alignment = Alignment.BottomEnd),
        onClick = { onClick() }
    ) {
        Image(
            painter = painterResource(R.drawable.baseline_add_24),
            contentDescription = "Icon"
        )
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
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(text = album.name)
            }
        }
    }
}
