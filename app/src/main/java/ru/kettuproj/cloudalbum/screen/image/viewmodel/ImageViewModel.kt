package ru.kettuproj.cloudalbum.screen.image.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.kettuproj.cloudalbum.model.Image
import ru.kettuproj.cloudalbum.repository.ImageRepo
import ru.kettuproj.cloudalbum.settings.Settings

class ImageViewModel (application: Application) : AndroidViewModel(application)  {

    private var context   = application
    private val token = MutableStateFlow(null as String?)

    val image = MutableStateFlow<Image?>(null)
    val isDeleted = MutableStateFlow<Boolean>(false)

    init {
        token.value = Settings.getToken(context)
    }

    fun loadImage(uuid: String){
        GlobalScope.launch {
            if(token.value!=null) {
                val data = ImageRepo.getImageByUUID(token.value.toString(), uuid)
                image.value = data.data
            }
        }
    }

    fun deleteImage(uuid: String){
        GlobalScope.launch {
            if(token.value!=null) {
                isDeleted.value = ImageRepo.deleteImage(token.value.toString(), uuid).data
            }
        }
    }
}