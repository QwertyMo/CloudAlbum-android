package ru.kettuproj.cloudalbum.common

import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.gestures.ScrollableState
import kotlinx.coroutines.awaitCancellation

suspend fun ScrollableState.setScrolling(value: Boolean) {
    scroll(scrollPriority = MutatePriority.PreventUserInput) {
        when (value) {
            true -> Unit
            else -> awaitCancellation()
        }
    }
}