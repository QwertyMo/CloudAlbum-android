package ru.kettuproj.cloudalbum.common.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import ru.kettuproj.cloudalbum.common.checkIsHostAvailable
import ru.kettuproj.cloudalbum.common.checkIsInternetAvailable

class NetworkViewModel(application: Application): AndroidViewModel(application)  {

    private val context = application

    private val isDone = MutableStateFlow(false)
    private val netAvailable = MutableStateFlow(false)
    private val hostAvailable = MutableStateFlow(false)

    init{
        isInternetAvailable()
        isHostAvailable()
        isDone.value = true
    }

    private fun isInternetAvailable(){
        netAvailable.value = checkIsInternetAvailable(context)
    }

    private fun isHostAvailable(){
        hostAvailable.value = checkIsHostAvailable()
    }
}