package ru.kettuproj.cloudalbum.ui.component.button

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.kettuproj.cloudalbum.R

@Composable
fun BoxScope.ButtonAddImage(galleryLauncher: ManagedActivityResultLauncher<String, Uri?>){
    FloatingActionButton(
        onClick = { galleryLauncher.launch("image/*")},
        modifier = Modifier
            .padding(all = 16.dp)
            .align(alignment = Alignment.BottomEnd),
    ) {
        Image(
            painter = painterResource(R.drawable.baseline_add_24),
            contentDescription = "Icon"
        )
    }
}

@Composable
fun galleryLauncher(
    onLoad: (byteArray: ByteArray) -> Unit
): ManagedActivityResultLauncher<String, Uri?> {
    val context = LocalContext.current
    return rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { url ->
        if(url!=null){
            val inputStream = context.contentResolver.openInputStream(url)
            val byteArray = inputStream?.readBytes()
            if(byteArray!=null) onLoad(byteArray)
            inputStream?.close()
        }

    }
}