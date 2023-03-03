package ru.kettuproj.cloudalbum.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import ru.kettuproj.cloudalbum.R
import ru.kettuproj.cloudalbum.screen.Destination
import ru.kettuproj.cloudalbum.screen.login.viewmodel.LoginViewModel
import ru.kettuproj.cloudalbum.screen.navigate

@Composable
fun LoginScreen(navController: NavController){
    val loginViewModel: LoginViewModel = viewModel()

    val login = remember{mutableStateOf("")}
    val password = remember{mutableStateOf("")}

    LaunchedEffect(Unit){
        loginViewModel.isProcessed.collectLatest { isProcessed ->
            loginViewModel.isAuth.collectLatest { isAuth ->
                if(!isProcessed) return@collectLatest
                if(isAuth)
                    navController.navigate(Destination.USER_IMAGES){
                        popUpTo(navController.graph.id){
                            inclusive =  true
                        }
                    }
                else loginViewModel.setAccepted()
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            modifier = Modifier.size(128.dp),
            painter = painterResource(R.drawable.baseline_photo_album_24),
            contentDescription = "Icon"
        )

        Text(text = "Login")
        TextField(value = login.value, onValueChange = {
            login.value = it
        })

        Text(text = "Password")
        TextField(value = password.value, onValueChange = {
            password.value = it
        })

        Button(onClick = {
            loginViewModel.login(login.value, password.value)
        }) {
            Text(text = "Login")
        }
    }


}