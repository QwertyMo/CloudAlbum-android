package ru.kettuproj.cloudalbum.screen.myProfile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.DelicateCoroutinesApi
import ru.kettuproj.cloudalbum.R
import ru.kettuproj.cloudalbum.common.getStatusBarSize
import ru.kettuproj.cloudalbum.model.Image
import ru.kettuproj.cloudalbum.screen.Destination
import ru.kettuproj.cloudalbum.screen.navigate
import ru.kettuproj.cloudalbum.screen.myProfile.viewmodel.ImagesViewModel
import ru.kettuproj.cloudalbum.ui.component.button.ButtonAddImage
import ru.kettuproj.cloudalbum.ui.component.button.galleryLauncher
import ru.kettuproj.cloudalbum.ui.component.grid.ImageGrid
import ru.kettuproj.cloudalbum.ui.component.image.ProfileImage

@Composable
@DelicateCoroutinesApi
fun MyProfileScreen(navController: NavController, paddings: PaddingValues){

    val viewModel: ImagesViewModel = viewModel()
    val galleryLauncher = galleryLauncher{ viewModel.uploadImage(it) }

    val images  = viewModel.images as List<Image>
    val user    = viewModel.user.collectAsState()
    val loaded  = viewModel.loaded.collectAsState()

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
                        user = user.value,
                        size = 96,
                    )
                    FloatingActionButton(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(paddingValues = PaddingValues(end = 16.dp, bottom = 16.dp))
                            .align(alignment = Alignment.BottomEnd),
                        onClick = { logout(viewModel, navController) }
                    ) {
                        Image(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(R.drawable.baseline_edit_24),
                            contentDescription = "Icon"
                        )
                    }
                }
            }

            ImageGrid(
                navController = navController,
                token = viewModel.getToken(),
                images = images,
                isLoaded = loaded.value
            )
        }
        ButtonAddImage(galleryLauncher)
    }
}

@DelicateCoroutinesApi
fun logout(viewModelImage: ImagesViewModel, navController: NavController){
    viewModelImage.logout()
    navController.navigate(Destination.LOGIN){
        popUpTo(navController.graph.id){
            inclusive = true
        }
    }
}