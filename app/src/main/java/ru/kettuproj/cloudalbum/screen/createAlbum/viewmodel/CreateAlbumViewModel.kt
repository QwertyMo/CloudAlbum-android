package ru.kettuproj.cloudalbum.screen.createAlbum.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.kettuproj.cloudalbum.model.Album
import ru.kettuproj.cloudalbum.repository.CARepository
import ru.kettuproj.cloudalbum.settings.Settings

class CreateAlbumViewModel (application: Application) : AndroidViewModel(application) {


    private var context   = application
    private val token = MutableStateFlow(null as String?)

    val isCreated = MutableStateFlow(false)
    val isProcessed = MutableStateFlow(false)

    val createdAlbum =  MutableStateFlow<Album?>(null)

    init{
        token.value = Settings.getToken(context)
    }

    fun create(name: String){
        isProcessed.value = false
        if(name.isEmpty()) return
        val t = token.value ?: return
        GlobalScope.launch {
            val data = CARepository.createAlbum(t, name)
            if(data.data!=null){
                createdAlbum.value = data.data
                isCreated.value = true
            }
            isProcessed.value = true
        }
    }

    fun setAccepted(){
        isProcessed.value = false
    }
}