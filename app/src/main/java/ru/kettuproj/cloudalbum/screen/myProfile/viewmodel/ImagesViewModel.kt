package ru.kettuproj.cloudalbum.screen.myProfile.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.kettuproj.cloudalbum.model.Image
import ru.kettuproj.cloudalbum.model.User
import ru.kettuproj.cloudalbum.repository.AuthRepo
import ru.kettuproj.cloudalbum.repository.ImageRepo
import ru.kettuproj.cloudalbum.settings.Settings

@DelicateCoroutinesApi
class ImagesViewModel (application: Application) : AndroidViewModel(application)  {

    private var context   = application
    private val token     = MutableStateFlow(null as String?)

    val images  = mutableStateListOf<Image>()
    val user    = MutableStateFlow<User?>(null)
    val loaded  = MutableStateFlow(false)
    init {
        token.value = Settings.getToken(context)
        loadUser()
        getAllImages()
    }

    private fun getAllImages(){
        GlobalScope.launch {
            if(token.value!=null) {
                val data = ImageRepo.getMyImages(token.value.toString())
                images.addAll(data.data)
                loaded.value = true
            }
        }
    }

    private fun loadUser(){
        GlobalScope.launch {
            if(token.value!=null) {
                val data = AuthRepo.getMe(token.value.toString())
                user.value = data.data
            }
        }
    }

    fun uploadImage(img: ByteArray){
        GlobalScope.launch{
            if(token.value!=null) {
                val data = ImageRepo.uploadImage(token.value.toString(), img)
                if (data.data != null) images.add(data.data)
            }
        }
    }

    fun logout(){
        Settings.deleteToken(context)
    }

    fun getToken():String?{
        return token.value
    }

}