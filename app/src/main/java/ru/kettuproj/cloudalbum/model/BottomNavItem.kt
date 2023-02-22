package ru.kettuproj.cloudalbum.model

import androidx.annotation.StringRes
import ru.kettuproj.cloudalbum.R
import ru.kettuproj.cloudalbum.screen.Destination

sealed class BottomNavItem(
                val route:      Destination,
                val icon:       Int,
    @StringRes  val resourceId: Int){

    object Profile : BottomNavItem(Destination.USER_IMAGES, R.drawable.ic_launcher_foreground, R.string.profile)
    object Albums  : BottomNavItem(Destination.ALBUMS, R.drawable.ic_launcher_foreground, R.string.albums)
}
