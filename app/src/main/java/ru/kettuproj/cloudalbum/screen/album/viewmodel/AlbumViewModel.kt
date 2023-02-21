package ru.kettuproj.cloudalbum.screen.album.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.kettuproj.cloudalbum.model.Image
import ru.kettuproj.cloudalbum.repository.CARepository
import ru.kettuproj.cloudalbum.settings.Settings
import java.io.File

class AlbumViewModel (application: Application) : AndroidViewModel(application)  {

    private var context   = application
    private val token = MutableStateFlow(null as String?)

    val images = mutableStateListOf<Image>()

    private var id: Int? = null

    init {
        token.value = Settings.getToken(context)
        getAllImages()
    }

    private fun getAllImages(){
        GlobalScope.launch {
            if(token.value!=null && id!=null) {
                val data = CARepository.getAlbumImages(token.value.toString(), id!!)
                images.addAll(data.data)
            }
        }
    }

    fun uploadImage(img: ByteArray){
        GlobalScope.launch{
            if(token.value!=null && id!=null) {
                val data = CARepository.uploadImage(token.value.toString(), img, id)
                if (data.data != null) images.add(data.data)
            }
        }
    }

    fun setID(id: String){
        this.id = id.toInt()
    }
}