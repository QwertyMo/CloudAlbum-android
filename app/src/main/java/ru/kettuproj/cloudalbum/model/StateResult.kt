package ru.kettuproj.cloudalbum.model

sealed class StateResult<out T> {
    object Loading : StateResult<Nothing>()
    object Error : StateResult<Nothing>()
    class Success<T>(val data: T?) : StateResult<T>()
}