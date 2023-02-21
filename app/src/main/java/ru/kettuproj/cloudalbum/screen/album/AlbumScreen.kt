package ru.kettuproj.cloudalbum.screen.album

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.kettuproj.cloudalbum.model.Image
import ru.kettuproj.cloudalbum.screen.Destination
import ru.kettuproj.cloudalbum.screen.album.viewmodel.AlbumViewModel
import ru.kettuproj.cloudalbum.screen.navigate
import ru.kettuproj.cloudalbum.ui.component.AlbumImage

@Composable
fun AlbumScreen(navController: NavController, albumID: String?){
    val context = LocalContext.current
    if(albumID==null) return
    val viewModelAlbum: AlbumViewModel = viewModel()
    viewModelAlbum.setID(albumID)
    val galleryLauncher = GalleryLauncher(viewModelAlbum)

    val images = viewModelAlbum.images as List<Image>

    Column {
        Button(onClick = { galleryLauncher.launch("image/*") }) {
            Text(text = "Upload")
        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp),
            modifier = Modifier.fillMaxHeight()){
            items(items = images, itemContent = {
                AlbumImage(
                    onClick = { navController.navigate(Destination.IMAGE, it.uuid) },
                    image = it
                )
            })
        }
    }
}

@Composable
fun GalleryLauncher(viewModelImage: AlbumViewModel): ManagedActivityResultLauncher<String, Uri?> {
    val context = LocalContext.current
    return rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { url ->
        if(url!=null){
            val inputStream = context.contentResolver.openInputStream(url)
            val byteArray = inputStream?.readBytes()
            if(byteArray!=null) viewModelImage.uploadImage(byteArray)
        }

    }
}
