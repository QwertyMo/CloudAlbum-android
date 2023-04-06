package ru.kettuproj.cloudalbum.screen.myProfile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import kotlinx.coroutines.DelicateCoroutinesApi
import ru.kettuproj.cloudalbum.R
import ru.kettuproj.cloudalbum.common.getStatusBarSize
import ru.kettuproj.cloudalbum.model.Image
import ru.kettuproj.cloudalbum.screen.Destination
import ru.kettuproj.cloudalbum.screen.navigate
import ru.kettuproj.cloudalbum.screen.myProfile.viewmodel.ImagesViewModel
import ru.kettuproj.cloudalbum.ui.component.animation.Shimmer
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
    val user    = viewModel.user.collectAsState().value
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
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Box{
                    ProfileImage(
                        modifier = Modifier.padding(16.dp),
                        user = user,
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
                            painter = painterResource(R.drawable.baseline_logout_24),
                            contentDescription = "Icon"
                        )
                    }
                }
                Box(
                    contentAlignment = Alignment.Center
                ){
                    Column{
                        if(loaded.value && user!=null){
                            Text(
                                text = user.login,
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

            if(!loaded.value || (user!=null && images.isNotEmpty())){
                ImageGrid(
                    navController = navController,
                    token = viewModel.getToken(),
                    images = images,
                    isLoaded = loaded.value,
                    loadCount = images.size
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

@DelicateCoroutinesApi
fun logout(viewModelImage: ImagesViewModel, navController: NavController){
    viewModelImage.logout()
    navController.navigate(Destination.LOGIN){
        popUpTo(navController.graph.id){
            inclusive = true
        }
    }
}