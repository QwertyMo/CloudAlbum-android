package ru.kettuproj.cloudalbum.screen.login.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.kettuproj.cloudalbum.repository.AuthRepo
import ru.kettuproj.cloudalbum.settings.Settings

class LoginViewModel(application: Application) : AndroidViewModel(application){
    private var context   = application

    val isAuth = MutableStateFlow(false)
    val isProcessed = MutableStateFlow(false)

    fun login(login: String, pass: String){
        isProcessed.value = false
        GlobalScope.launch {
            val data = AuthRepo.postLogin(login, pass)
            if(data.data!=null) {
                Settings.setToken(context, data.data.token)
                isAuth.value = true
            }
            isProcessed.value = true
        }
    }

    fun setAccepted(){
        isProcessed.value = false
    }
}