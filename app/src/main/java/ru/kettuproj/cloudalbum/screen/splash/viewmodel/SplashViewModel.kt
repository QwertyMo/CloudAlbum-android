package ru.kettuproj.cloudalbum.screen.splash.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.kettuproj.cloudalbum.repository.CARepository
import ru.kettuproj.cloudalbum.settings.Settings

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
            val user = CARepository.getMe(token.value.toString()).data
            if(user!=null) isAuthorized.value = true
            endScreen.value = true
        }
    }

}