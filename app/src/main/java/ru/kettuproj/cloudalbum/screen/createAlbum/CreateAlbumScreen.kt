package ru.kettuproj.cloudalbum.screen.createAlbum

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import ru.kettuproj.cloudalbum.R
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

        val fontSize = 18.sp

        Text(
            text = "Album name",
            fontSize = fontSize
        )
        OutlinedTextField(
            value = name.value,
            modifier = Modifier.padding(8.dp),
            onValueChange = {
                name.value = it
            }
        )

        Spacer(modifier = Modifier.padding(4.dp))

        Button(
            onClick = { viewModel.create(name.value) },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "Create",
                fontSize = fontSize,
                modifier = Modifier
                    .padding(2.dp)
            )
        }
    }
}