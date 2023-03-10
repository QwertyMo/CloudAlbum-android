package ru.kettuproj.cloudalbum.screen.createAlbum

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import ru.kettuproj.cloudalbum.screen.Destination
import ru.kettuproj.cloudalbum.screen.createAlbum.viewmodel.CreateAlbumViewModel
import ru.kettuproj.cloudalbum.screen.navigate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAlbumScreen(navController: NavController) {
    val viewModel: CreateAlbumViewModel = viewModel()

    val name = remember{ mutableStateOf("") }

    LaunchedEffect(Unit){
        viewModel.isProcessed.collectLatest { isProcessed ->
            viewModel.isCreated.collectLatest { isCreated ->
                val album = viewModel.createdAlbum.value ?: return@collectLatest
                if(!isProcessed) return@collectLatest
                if(isCreated){
                    navController.popBackStack()
                    navController.popBackStack()
                    navController.navigate(Destination.ALBUMS)
                    navController.navigate(Destination.ALBUM, album.id.toString())
                }
                else viewModel.setAccepted()
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = "Name")
        TextField(value = name.value, onValueChange = {
            name.value = it
        })

        Button(onClick = {
            viewModel.create(name.value)
        }) {
            Text(text = "Create")
        }
    }
}