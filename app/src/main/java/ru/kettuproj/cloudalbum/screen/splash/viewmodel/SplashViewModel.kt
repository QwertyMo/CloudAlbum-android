package ru.kettuproj.cloudalbum.screen.splash.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.kettuproj.cloudalbum.repository.AuthRepo
import ru.kettuproj.cloudalbum.settings.Settings

@DelicateCoroutinesApi
class SplashViewModel (application: Application) : AndroidViewModel(application)  {

    private var context   = application
    private val token = MutableStateFlow(null as String?)

    val endScreen = MutableStateFlow(false)
    val isAuthorized = MutableStateFlow(false)

    init{
        token.value = Settings.getToken(context)
        if(token.value == null) endScreen.value = true
        else checkAuth()

    }

    private fun checkAuth(){
        GlobalScope.launch {
            val user = AuthRepo.getMe(token.value.toString()).data
            if(user!=null) isAuthorized.value = true
            endScreen.value = true
        }
    }

}