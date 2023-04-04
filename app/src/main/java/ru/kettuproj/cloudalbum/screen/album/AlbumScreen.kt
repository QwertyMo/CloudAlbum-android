package ru.kettuproj.cloudalbum.screen.album

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.kettuproj.cloudalbum.R
import ru.kettuproj.cloudalbum.common.getStatusBarSize
import ru.kettuproj.cloudalbum.model.Image
import ru.kettuproj.cloudalbum.screen.album.viewmodel.AlbumViewModel
import ru.kettuproj.cloudalbum.screen.myProfile.logout
import ru.kettuproj.cloudalbum.ui.component.button.ButtonAddImage
import ru.kettuproj.cloudalbum.ui.component.grid.ImageGrid
import ru.kettuproj.cloudalbum.ui.component.image.ProfileImage

@Composable
fun AlbumScreen(navController: NavController, paddings: PaddingValues, albumID: String?){
    if(albumID==null) return

    val viewModel: AlbumViewModel = viewModel()
    viewModel.setID(albumID)
    val galleryLauncher = GalleryLauncher(viewModel)

    val images  = viewModel.images as List<Image>
    val loaded  = viewModel.loaded.collectAsState()
    val album   = viewModel.album.collectAsState()

    Box(modifier = Modifier
        .padding(
            PaddingValues(
                top = getStatusBarSize(LocalContext.current.resources)
            )
        )
        .fillMaxSize()
        .statusBarsPadding()
        .navigationBarsPadding()
        .padding(paddings)) {
        Column {
            Row(modifier = Modifier.fillMaxWidth()){
                Box{
                    ProfileImage(
                        modifier = Modifier.padding(16.dp),
                        album = album.value,
                        size = 96,
                    )
                    FloatingActionButton(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(paddingValues = PaddingValues(end = 16.dp, bottom = 16.dp))
                            .align(alignment = Alignment.BottomEnd),
                        onClick = {  }
                    ) {
                        Image(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(R.drawable.baseline_delete_24),
                            contentDescription = "Icon"
                        )
                    }
                }
            }
            ImageGrid(
                navController = navController,
                token = viewModel.getToken(),
                images = images,
                albumID = albumID.toInt(),
                isLoaded = loaded.value
            )
        }
        ButtonAddImage(galleryLauncher)
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
