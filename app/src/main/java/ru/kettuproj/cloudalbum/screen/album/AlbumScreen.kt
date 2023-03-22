package ru.kettuproj.cloudalbum.screen.album

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.kettuproj.cloudalbum.model.Image
import ru.kettuproj.cloudalbum.screen.album.viewmodel.AlbumViewModel
import ru.kettuproj.cloudalbum.ui.component.grid.ImageGrid

@Composable
fun AlbumScreen(navController: NavController, albumID: String?){
    if(albumID==null) return

    val viewModel: AlbumViewModel = viewModel()
    viewModel.setID(albumID)
    val galleryLauncher = GalleryLauncher(viewModel)

    val images = viewModel.images as List<Image>

    Column {
        Button(onClick = { galleryLauncher.launch("image/*") }) {
            Text(text = "Upload")
        }


        ImageGrid(
            navController = navController,
            token = viewModel.getToken(),
            images = images,
            albumID = albumID.toInt()
        )

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
