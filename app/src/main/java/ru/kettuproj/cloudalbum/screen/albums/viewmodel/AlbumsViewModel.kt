package ru.kettuproj.cloudalbum.screen.albums.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.kettuproj.cloudalbum.common.Constant
import ru.kettuproj.cloudalbum.model.Album
import ru.kettuproj.cloudalbum.model.Image
import ru.kettuproj.cloudalbum.repository.AlbumRepo
import ru.kettuproj.cloudalbum.repository.ImageRepo
import ru.kettuproj.cloudalbum.settings.Settings

class AlbumsViewModel (application: Application) : AndroidViewModel(application)  {

    private var context   = application
    private val token = MutableStateFlow(null as String?)

    val loaded  = MutableStateFlow(false)
    val albums = mutableStateListOf<Album>()
    val previews = mutableMapOf<Int, List<Image>>()

    init {
        token.value = Settings.getToken(context)
        loadAlbums()

    }

    @DelicateCoroutinesApi
    private fun loadAlbums(){
        GlobalScope.launch {
            if(token.value!=null) {
                val data = AlbumRepo.getMyAlbums(token.value.toString())
                albums.addAll(data.data)
                loadPreviewImages(data.data)
            }
        }
    }

    @DelicateCoroutinesApi
    private fun loadPreviewImages(albums: List<Album>){
        GlobalScope.launch {
            if(token.value!=null){
                for(album in albums){
                    val images =
                        if(album.images>=4) 4
                        else album.images
                    if(images != 0){
                        val data = ImageRepo.getPosedImages(token.value.toString(),0, images-1, album.id)
                        previews[album.id] = data.data
                    }
                }
                loaded.value = true
            }
        }
    }

    fun getToken():String?{
        return token.value
    }

}