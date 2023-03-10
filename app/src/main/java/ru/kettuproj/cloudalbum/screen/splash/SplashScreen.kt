package ru.kettuproj.cloudalbum.screen.splash

import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.core.splashscreen.SplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import ru.kettuproj.cloudalbum.R
import ru.kettuproj.cloudalbum.common.viewmodel.NetworkViewModel
import ru.kettuproj.cloudalbum.screen.Destination
import ru.kettuproj.cloudalbum.screen.navigate
import ru.kettuproj.cloudalbum.screen.splash.viewmodel.SplashViewModel
import ru.kettuproj.cloudalbum.ui.component.alert.DialogWindow

@Composable
fun SplashScreen(navController: NavController, splash: SplashScreen){

    checkNetwork(splash){
        val viewModel: SplashViewModel = viewModel()
        LaunchedEffect(Unit){
            viewModel.endScreen.collectLatest { isEnd ->
                if(isEnd){
                    viewModel.isAuthorized.collectLatest { isAuth ->
                        if(isAuth){
                            navController.navigate(Destination.MY_PROFILE){
                                popUpTo(navController.graph.id){
                                    inclusive = true
                                }
                            }
                        }
                        else{
                            navController.navigate(Destination.LOGIN){
                                popUpTo(navController.graph.id){
                                    inclusive = true
                                }
                            }
                        }
                        splash.setKeepOnScreenCondition { false }
                    }
                }
            }
        }
    }
}

@Composable
fun checkNetwork(
    splash: SplashScreen,
    onSuccess: @Composable () -> Unit = {}
){
    val network: NetworkViewModel = viewModel()
    val opened = remember { mutableStateOf(false) }

    val isNetAvailable = network.netAvailable.collectAsState()
    val isHostAvailable = network.hostAvailable.collectAsState()
    val isEnd = network.isDone.collectAsState()

    if(isEnd.value){
        println("netstat: $isNetAvailable $isHostAvailable")
        if(!isNetAvailable.value){
            opened.value = true
            if(opened.value){
                splash.setKeepOnScreenCondition { false }
                DialogWindow(
                    onClose = {
                        opened.value = false
                        network.checkConnection()
                        splash.setKeepOnScreenCondition { true }
                    },
                    icon = painterResource(id = R.drawable.baseline_error_outline_24),
                    text = "No internet connection. Please. turn on internet on your device",
                    buttonText = "Retry"
                )
            }
        }
        else if(!isHostAvailable.value){
            opened.value = true
            if(opened.value){
                splash.setKeepOnScreenCondition { false }
                DialogWindow(
                    onClose = {
                        opened.value = false
                        network.checkConnection()
                        splash.setKeepOnScreenCondition { true }
                    },
                    icon = painterResource(id = R.drawable.baseline_error_outline_24),
                    text = "Can't connect to server. Please, try again later",
                    buttonText = "Retry"
                )
            }
        }
        else{
            onSuccess()
        }
    }

}