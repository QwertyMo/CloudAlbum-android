package ru.kettuproj.cloudalbum.screen.splash

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.core.splashscreen.SplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import ru.kettuproj.cloudalbum.R
import ru.kettuproj.cloudalbum.screen.Destination
import ru.kettuproj.cloudalbum.screen.navigate
import ru.kettuproj.cloudalbum.screen.splash.viewmodel.SplashViewModel

@Composable
fun SplashScreen(navController: NavController, splash: SplashScreen){

    val splashViewModel: SplashViewModel = viewModel()

    LaunchedEffect(Unit){
        splashViewModel.endScreen.collectLatest { isEnd ->
            if(isEnd){
                splashViewModel.isAuthorized.collectLatest { isAuth ->
                    if(isAuth){
                        navController.navigate(Destination.USER_IMAGES){
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