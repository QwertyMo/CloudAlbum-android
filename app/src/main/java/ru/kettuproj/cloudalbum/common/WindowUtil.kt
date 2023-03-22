package ru.kettuproj.cloudalbum.common

import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.compose.ui.unit.Dp

@SuppressLint("InternalInsetResource", "DiscouragedApi")
fun getStatusBarSize(resources: Resources): Dp{
    var result = 0f
    val resourceId: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimension(resourceId)
    }
    return Dp(result / resources.displayMetrics.density)
}

@SuppressLint("InternalInsetResource", "DiscouragedApi")
fun getBottomBarSize(resources: Resources): Dp{
    var result = 0f
    val resourceId: Int = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimension(resourceId)
    }
    return Dp(result / resources.displayMetrics.density)
}