package ru.kettuproj.cloudalbum.screen.albums

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.kettuproj.cloudalbum.R
import ru.kettuproj.cloudalbum.common.getStatusBarSize
import ru.kettuproj.cloudalbum.model.Album
import ru.kettuproj.cloudalbum.model.Image
import ru.kettuproj.cloudalbum.model.Token
import ru.kettuproj.cloudalbum.screen.Destination
import ru.kettuproj.cloudalbum.screen.albums.viewmodel.AlbumsViewModel
import ru.kettuproj.cloudalbum.screen.navigate
import ru.kettuproj.cloudalbum.ui.component.image.AlbumImage

@Composable
fun AlbumsScreen(navController: NavController, paddings: PaddingValues){

    val context = LocalContext.current

    val viewModel: AlbumsViewModel = viewModel()
    val images = viewModel.albums as List<Album>

    val previews = viewModel.previews as Map<Int, List<Image>>
    val loaded  = viewModel.loaded.collectAsState()

    if(loaded.value){
        Box(modifier = Modifier.fillMaxSize().padding(paddings)){
            if(images.isEmpty()){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier
                            .padding(60.dp),
                        textAlign = TextAlign.Center,
                        text = "No albums. Press + to create your first album",
                        lineHeight = 30.sp,
                        fontSize = 24.sp
                    )
                }
            }
            else AlbumsGrid(navController, previews, images, PaddingValues(0.dp), viewModel.getToken())
            ButtonCreateAlbum(onClick = {
                navController.navigate(Destination.CREATE_ALBUM)
            })
        }
    }
    else{
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            CircularProgressIndicator()
        }
    }
}

@Composable
fun AlbumsGrid(
    navController: NavController,
    previews: Map<Int, List<Image>>,
    images: List<Album>,
    paddings: PaddingValues,
    token: String?)
{
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
                columns = GridCells.Adaptive(minSize = 192.dp),
                modifier = Modifier.fillMaxHeight()){
                itemsIndexed(items = images){ index, item ->
                    AlbumElement(album = item, navController = navController, previews[item.id], token = token)
                }
            }
        }

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
fun AlbumElement(
    album: Album,
    navController: NavController,
    preview: List<Image>?,
    token: String?)
{
    Box(
        modifier = Modifier
            .padding(4.dp)
            .height(192.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate(Destination.ALBUM, album.id.toString())
            }
    ){
        Card(
            modifier = Modifier.fillMaxSize()
        ) {
            Box{
                if(preview!=null){
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 72.dp),
                        modifier = Modifier.fillMaxHeight(),
                        userScrollEnabled = false){
                        itemsIndexed(items = preview){ index, item ->
                            AlbumImage(
                                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                                image = item,
                                token = token,
                                size = 96,
                                padding = 0,
                                clickable = false)
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ){
                    val images =
                        if(album.images == 0) "Empty album"
                        else "${album.images} images"
                    Text(
                        text = album.name,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        fontSize = 28.sp
                    )
                    Text(
                        text = images,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}
