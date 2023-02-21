package ru.kettuproj.cloudalbum.screen.albums.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.kettuproj.cloudalbum.model.Album
import ru.kettuproj.cloudalbum.model.Image
import ru.kettuproj.cloudalbum.repository.CARepository
import ru.kettuproj.cloudalbum.settings.Settings

class AlbumsViewModel (application: Application) : AndroidViewModel(application)  {

    private var context   = application
    private val token = MutableStateFlow(null as String?)

    val albums = mutableStateListOf<Album>()
    val isDeleted = MutableStateFlow<Boolean>(false)

    init {
        token.value = Settings.getToken(context)
        loadAlbums()
    }

    private fun loadAlbums(){
        GlobalScope.launch {
            if(token.value!=null) {
                val data = CARepository.getMyAlbums(token.value.toString())
                albums.addAll(data.data)
            }
        }
    }

}