package ru.kettuproj.cloudalbum.common.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.kettuproj.cloudalbum.common.checkIsHostAvailable
import ru.kettuproj.cloudalbum.common.checkIsInternetAvailable

class NetworkViewModel(application: Application): AndroidViewModel(application)  {

    private val context = application

    val isDone = MutableStateFlow(false)
    val netAvailable = MutableStateFlow(false)
    val hostAvailable = MutableStateFlow(false)

    init{
        checkConnection()
    }

    fun checkConnection(){
        GlobalScope.launch {
            isInternetAvailable()
            isHostAvailable()
            isDone.value = true
        }
    }

    private fun isInternetAvailable(){
        netAvailable.value = checkIsInternetAvailable(context)
    }

    private suspend fun isHostAvailable(){
        hostAvailable.value = checkIsHostAvailable()
    }
}