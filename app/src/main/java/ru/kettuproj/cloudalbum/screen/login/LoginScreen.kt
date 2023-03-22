package ru.kettuproj.cloudalbum.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import ru.kettuproj.cloudalbum.R
import ru.kettuproj.cloudalbum.screen.Destination
import ru.kettuproj.cloudalbum.screen.login.viewmodel.LoginViewModel
import ru.kettuproj.cloudalbum.screen.navigate
import ru.kettuproj.cloudalbum.ui.component.alert.DialogWindow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController){
    val viewModel: LoginViewModel = viewModel()

    val login = remember{mutableStateOf("")}
    val password = remember{mutableStateOf("")}
    val showDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        viewModel.isProcessed.collectLatest { isProcessed ->
            viewModel.isAuth.collectLatest { isAuth ->
                if(!isProcessed) return@collectLatest
                if(isAuth)
                    navController.navigate(Destination.MY_PROFILE){
                        popUpTo(navController.graph.id){
                            inclusive =  true
                        }
                    }
                else {
                    showDialog.value = true
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        if(showDialog.value){
            DialogWindow(
                onClose = {
                    viewModel.setAccepted()
                    showDialog.value = false
                },
                icon = painterResource(R.drawable.baseline_error_outline_24),
                text = "Wrong login or password",
                buttonText = "Ok"
            )
        }

        val passwordVisible = remember { mutableStateOf(false) }
        val fontSize = 18.sp
        Icon(
            modifier = Modifier
                .size(160.dp)
                .padding(24.dp),
            painter = painterResource(R.drawable.baseline_photo_album_24),
            contentDescription = "Icon",
            tint = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Login",
            fontSize = fontSize
        )
        OutlinedTextField(
            value = login.value,
            modifier = Modifier.padding(8.dp),
            onValueChange = {
                login.value = it
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.baseline_person_24),
                    contentDescription = null
                )
            }
        )

        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = "Password",
            fontSize = fontSize
        )
        OutlinedTextField(
            visualTransformation =
                if (passwordVisible.value) VisualTransformation.None
                else PasswordVisualTransformation(),
            value = password.value,
            modifier = Modifier.padding(8.dp),
            onValueChange = {
                password.value = it
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.baseline_lock_24),
                    contentDescription = null
                )
            },
            trailingIcon = {
                val painter =
                    if(passwordVisible.value) painterResource(R.drawable.baseline_visibility_24)
                    else painterResource(R.drawable.baseline_visibility_off_24)
                IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                    Icon(painter = painter, contentDescription = null)
                }
            }
        )

        Button(
            onClick = { viewModel.login(login.value, password.value) },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "Login",
                fontSize = fontSize,
                modifier = Modifier
                    .padding(2.dp)
            )
        }
    }


}