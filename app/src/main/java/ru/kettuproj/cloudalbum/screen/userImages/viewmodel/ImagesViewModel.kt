package ru.kettuproj.cloudalbum.screen.userImages.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.kettuproj.cloudalbum.model.Image
import ru.kettuproj.cloudalbum.repository.ImageRepo
import ru.kettuproj.cloudalbum.settings.Settings

class ImagesViewModel (application: Application) : AndroidViewModel(application)  {

    private var context   = application
    private val token = MutableStateFlow(null as String?)

    val images = mutableStateListOf<Image>()

    init {
        token.value = Settings.getToken(context)
        getAllImages()
    }

    private fun getAllImages(){
        GlobalScope.launch {
            if(token.value!=null) {
                val data = ImageRepo.getMyImages(token.value.toString())
                images.addAll(data.data)
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