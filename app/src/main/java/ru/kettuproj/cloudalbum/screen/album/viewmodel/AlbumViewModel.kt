package ru.kettuproj.cloudalbum.screen.album.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.kettuproj.cloudalbum.model.Image
import ru.kettuproj.cloudalbum.repository.AlbumRepo
import ru.kettuproj.cloudalbum.repository.ImageRepo
import ru.kettuproj.cloudalbum.settings.Settings

class AlbumViewModel (application: Application) : AndroidViewModel(application)  {

    private var context   = application
    private val token = MutableStateFlow(null as String?)

    val images = mutableStateListOf<Image>()

    private var id: Int? = null

    init {
        token.value = Settings.getToken(context)
        getAllImages()
    }

    @DelicateCoroutinesApi
    private fun getAllImages(){
        GlobalScope.launch {
            if(token.value!=null && id!=null) {
                val data = AlbumRepo.getAlbumImages(token.value.toString(), id!!)
                images.addAll(data.data)
            }
        }
    }

    @DelicateCoroutinesApi
    fun uploadImage(img: ByteArray){
        GlobalScope.launch{
            if(token.value!=null && id!=null) {
                val data = ImageRepo.uploadImage(token.value.toString(), img, id)
                if (data.data != null) images.add(data.data)
            }
        }
    }

    fun setID(id: String){
        this.id = id.toInt()
    }

    fun getToken():String?{
        return token.value
    }
}