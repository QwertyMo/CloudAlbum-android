package ru.kettuproj.cloudalbum.screen.albums.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.kettuproj.cloudalbum.model.Album
import ru.kettuproj.cloudalbum.repository.AlbumRepo
import ru.kettuproj.cloudalbum.settings.Settings

@DelicateCoroutinesApi
class AlbumsViewModel (application: Application) : AndroidViewModel(application)  {

    private var context   = application
    private val token = MutableStateFlow(null as String?)

    val albums = mutableStateListOf<Album>()

    init {
        token.value = Settings.getToken(context)
        loadAlbums()
    }

    private fun loadAlbums(){
        GlobalScope.launch {
            if(token.value!=null) {
                val data = AlbumRepo.getMyAlbums(token.value.toString())
                albums.addAll(data.data)
            }
        }
    }

}