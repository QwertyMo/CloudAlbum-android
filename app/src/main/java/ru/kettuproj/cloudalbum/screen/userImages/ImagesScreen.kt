package ru.kettuproj.cloudalbum.screen.userImages

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import ru.kettuproj.cloudalbum.common.Constant
import ru.kettuproj.cloudalbum.model.Image
import ru.kettuproj.cloudalbum.screen.Destination
import ru.kettuproj.cloudalbum.screen.navigate
import ru.kettuproj.cloudalbum.screen.userImages.viewmodel.ImagesViewModel
import ru.kettuproj.cloudalbum.ui.component.AlbumImage


@Composable
fun ImagesScreen(navController: NavController){

    val context = LocalContext.current

    val viewModelImage: ImagesViewModel = viewModel()
    val galleryLauncher = GalleryLauncher(viewModelImage)

    val images = viewModelImage.images as List<Image>

    Column {
        Button(onClick = { galleryLauncher.launch("image/*") }) {
            Text(text = "Upload")
        }
        Button(onClick = { logout(viewModelImage, navController) }) {
            Text(text = "Logout")
        }
        Button(onClick = { navController.navigate(Destination.ALBUMS) }) {
            Text(text = "Albums")
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
fun GalleryLauncher(viewModelImage: ImagesViewModel): ManagedActivityResultLauncher<String, Uri?> {
    val context = LocalContext.current
    return rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { url ->
        if(url!=null){
            val inputStream = context.contentResolver.openInputStream(url)
            val byteArray = inputStream?.readBytes()
            if(byteArray!=null) viewModelImage.uploadImage(byteArray)
        }

    }
}

fun logout(viewModelImage: ImagesViewModel, navController: NavController){
    viewModelImage.logout()
    navController.navigate(Destination.LOGIN){
        popUpTo(navController.graph.id){
            inclusive = true
        }
    }
}