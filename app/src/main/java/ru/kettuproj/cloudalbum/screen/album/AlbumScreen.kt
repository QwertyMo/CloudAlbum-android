package ru.kettuproj.cloudalbum.screen.album

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import ru.kettuproj.cloudalbum.R
import ru.kettuproj.cloudalbum.common.getStatusBarSize
import ru.kettuproj.cloudalbum.model.Image
import ru.kettuproj.cloudalbum.repository.AlbumRepo
import ru.kettuproj.cloudalbum.screen.Destination
import ru.kettuproj.cloudalbum.screen.album.viewmodel.AlbumViewModel
import ru.kettuproj.cloudalbum.screen.myProfile.logout
import ru.kettuproj.cloudalbum.ui.component.animation.Shimmer
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
    val album   = viewModel.album.collectAsState().value

    LaunchedEffect(Unit){
        viewModel.deleted.collectLatest { isDeleted ->
            if(isDeleted){
                var route = navController.previousBackStackEntry?.destination?.route
                if(route != null){
                    val args = navController.previousBackStackEntry?.destination?.arguments
                    if(args!=null)
                        for(i in args){
                            route = route?.replace("{${i.key}}", navController.previousBackStackEntry?.arguments?.getString(i.key)!!)
                        }
                }
                else route = Destination.MY_PROFILE.dest
                navController.popBackStack()
                navController.popBackStack()
                navController.navigate(route!!)
            }
        }
    }

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
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically){
                Box{
                    ProfileImage(
                        modifier = Modifier.padding(16.dp),
                        album = album,
                        size = 96,
                    )
                    FloatingActionButton(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(paddingValues = PaddingValues(end = 16.dp, bottom = 16.dp))
                            .align(alignment = Alignment.BottomEnd),
                        onClick = {
                            viewModel.deleteAlbum()
                        }
                    ) {
                        Image(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(R.drawable.baseline_delete_24),
                            contentDescription = "Icon"
                        )
                    }
                }
                Box(
                    contentAlignment = Alignment.Center
                ){
                    Column{
                        if(loaded.value && album!=null){
                            Text(
                                text = album.name,
                                fontSize = 24.sp
                            )
                            Text(
                                text = "${images.size} images",
                                fontSize = 20.sp
                            )
                        }
                        else{
                            Shimmer(modifier = Modifier
                                .width(128.dp)
                                .clip(RoundedCornerShape(4.dp))){
                                Text(
                                    text = "1",
                                    color = Color.Transparent,
                                    fontSize = 24.sp
                                )
                            }
                            Spacer(modifier = Modifier.padding(2.dp))
                            Shimmer(modifier = Modifier
                                .width(96.dp)
                                .clip(RoundedCornerShape(4.dp))){
                                Text(
                                    text = "1",
                                    color = Color.Transparent,
                                    fontSize = 20.sp
                                )
                            }
                        }
                    }
                }
            }
            if(!loaded.value || (album!=null && images.isNotEmpty())){
                ImageGrid(
                    navController = navController,
                    token = viewModel.getToken(),
                    images = images,
                    albumID = albumID.toInt(),
                    isLoaded = loaded.value,
                    loadCount = album?.images ?: 33
                )
            }else{
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                    ) {
                    Text(
                        modifier = Modifier
                            .padding(60.dp),
                        textAlign = TextAlign.Center,
                        text = "No images. Press + to add your first image",
                        lineHeight = 30.sp,
                        fontSize = 24.sp
                        )
                }
            }
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
