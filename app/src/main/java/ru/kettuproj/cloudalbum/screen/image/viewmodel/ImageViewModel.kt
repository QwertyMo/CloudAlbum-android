package ru.kettuproj.cloudalbum.screen.image.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import io.ktor.http.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.kettuproj.cloudalbum.model.DataCount
import ru.kettuproj.cloudalbum.model.Image
import ru.kettuproj.cloudalbum.repository.ImageRepo
import ru.kettuproj.cloudalbum.settings.Settings

class ImageViewModel (application: Application) : AndroidViewModel(application)  {

    private var context   = application
    private var token: String? = null

    val dataCount = MutableStateFlow<DataCount?>(null)

    val image = MutableStateFlow<Image?>(null)
    val isDeleted = MutableStateFlow(false)
    val loaded = MutableStateFlow(0)

    private var images: Array<Image?>? = null

    init {
        token = Settings.getToken(context)
    }

    fun updateCurrent(pos: Int, albumID: Int?){
        GlobalScope.launch {
            image.value = loadImage(pos, albumID)
        }
    }

    fun getDataCount(albumID: Int? = null){
        GlobalScope.launch {
            if(token!=null) {
                val data = ImageRepo.getDataCount(token.toString(), albumID)
                dataCount.value = data.data
                if(images==null && data.data!=null)
                    images = arrayOfNulls(data.data.images)
            }
        }
    }

    suspend fun loadImage(pos: Int, albumID: Int? = null): Image? {

        if (token == null) return null
        if (images == null) return null

        return if (images!![pos] == null) {
            val data = ImageRepo.getPosedImage(token.toString(), pos, albumID)
            if (data.data != null) {
                images!![pos] = data.data
                loaded.value++
            }
            data.data
        } else images!![pos]
    }

    fun deleteImage(uuid: String){
        GlobalScope.launch {
            if(token!=null) {
                isDeleted.value = ImageRepo.deleteImage(token.toString(), uuid).status == HttpStatusCode.OK
            }
        }
    }

    fun getToken():String?{
        return token
    }
}