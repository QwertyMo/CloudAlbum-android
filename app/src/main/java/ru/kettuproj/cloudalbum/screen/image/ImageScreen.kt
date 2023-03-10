package ru.kettuproj.cloudalbum.screen.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.kettuproj.cloudalbum.common.Constant
import ru.kettuproj.cloudalbum.screen.Destination
import ru.kettuproj.cloudalbum.screen.image.viewmodel.ImageViewModel
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.file.Files


@Composable
fun ImageScreen(navController: NavController, imageUUID: String?) {

    if(imageUUID == null){
        navController.popBackStack()
        return
    }
    val imageViewModel: ImageViewModel = viewModel()
    imageViewModel.loadImage(imageUUID)
    val isDeleted = imageViewModel.isDeleted.collectAsState()
    val image = imageViewModel.image.collectAsState()

    if(isDeleted.value){
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
        return
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ){
        AsyncImage(
            modifier = Modifier.fillMaxWidth(),
            model = Constant.imageURL(image.value?.uuid.toString()),
            contentDescription = ""
        )
        Text(text = image.value?.created.toString())
        Button(onClick = {
            imageViewModel.deleteImage(imageUUID)
        }) {
            Text(text = "delete")
        }
        Button(onClick = {
            save(Constant.imageURL(image.value?.uuid.toString()))
        }) {
            Text(text = "save")
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
