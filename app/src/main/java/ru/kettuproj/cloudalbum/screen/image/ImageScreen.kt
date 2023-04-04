package ru.kettuproj.cloudalbum.screen.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.kettuproj.cloudalbum.R
import ru.kettuproj.cloudalbum.common.Constant
import ru.kettuproj.cloudalbum.common.Constant.imageURL
import ru.kettuproj.cloudalbum.common.getStatusBarSize
import ru.kettuproj.cloudalbum.model.Image
import ru.kettuproj.cloudalbum.model.StateResult
import ru.kettuproj.cloudalbum.screen.Destination
import ru.kettuproj.cloudalbum.screen.image.viewmodel.ImageViewModel
import ru.kettuproj.cloudalbum.ui.component.image.ZoomableAsyncImage
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.file.Files

@Composable
@DelicateCoroutinesApi
@ExperimentalPagerApi
fun ImageScreen(
    navController: NavController,
    posIn: String?,
    albumID: String?
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
    ){
        val album = albumID?.toInt()
        val viewModel: ImageViewModel = viewModel()
        viewModel.getDataCount(album)
        val stop = remember { mutableStateOf(false) }
        val pos = rememberPagerState(initialPage = posIn?.toInt() ?: 0)
        val visibleBar = remember { mutableStateOf(false) }

        val currentImage = viewModel.image.collectAsState().value

        LaunchedEffect(Unit){
            viewModel.isDeleted.collectLatest { isDeleted ->
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
                    stop.value = true
                }
            }
        }
        if(!stop.value){
            val counts = viewModel.dataCount.collectAsState().value
            if(pos.isScrollInProgress){
                visibleBar.value = false
            }
            else{
                viewModel.updateCurrent(pos.currentPage, album)
            }
            if(counts!=null){
                HorizontalPager(
                    count = counts.images,
                    state = pos
                ) {
                    val img = loadImage(it, album, viewModel).value
                    if (img is StateResult.Success<Image>) {
                        ZoomableAsyncImage(
                            image = img.data,
                            token = viewModel.getToken(),
                            scrollState = pos,
                            onClick = { visibleBar.value = !visibleBar.value}
                        )
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = visibleBar.value,
            enter = slideInVertically(),
            exit = slideOutVertically()
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)){
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        PaddingValues(
                            top = getStatusBarSize(LocalContext.current.resources)
                        )
                    ),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { if(currentImage!=null) viewModel.deleteImage(currentImage.uuid)}) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_delete_24),
                            contentDescription = "Icon",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    IconButton(onClick = { if(currentImage!=null) save(imageURL(currentImage.uuid))}) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_download_24),
                            contentDescription = "Icon",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun loadImage(
    pos: Int,
    albumID: Int? = null,
    viewModel: ImageViewModel,
): State<StateResult<Image>> {

    // Creates a State<T> with Result.Loading as initial value
    // If either `url` or `imageRepository` changes, the running producer
    // will cancel and will be re-launched with the new inputs.
    return produceState<StateResult<Image>>(initialValue = StateResult.Loading, pos, albumID, viewModel) {

        // In a coroutine, can make suspend calls
        val image = viewModel.loadImage(pos, albumID)

        // Update State with either an Error or Success result.
        // This will trigger a recomposition where this State is read
        value = if (image == null) {
            StateResult.Error
        } else {
            StateResult.Success(image)
        }
    }
}

fun save(url: String){
    GlobalScope.launch {
        val img = BitmapFactory.decodeStream(URL(url).openConnection().getInputStream())

        val imageFileName = System.currentTimeMillis().toString() + ".png"
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS).toString() + "/CloudAlbum")

        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }
        if (success) {
            val imageFile = File(storageDir, imageFileName)
            try {
                val fOut = FileOutputStream(imageFile)
                img.compress(Bitmap.CompressFormat.PNG, 100, fOut)
                fOut.close()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Files.move(imageFile.toPath(), imageFile.toPath())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
