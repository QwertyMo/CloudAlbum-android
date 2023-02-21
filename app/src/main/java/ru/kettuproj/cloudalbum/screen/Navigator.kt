package ru.kettuproj.cloudalbum.screen

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions

enum class Destination(val dest: String){
    LOGIN("login"),
    USER_IMAGES("user_images"),
    IMAGE("image"),
    SPLASH("splash"),
    ALBUMS("albums"),
    ALBUM("album"),
    CREATE_ALBUM("create_album")
}

fun NavController.navigate(dest: Destination, builder: NavOptionsBuilder.() -> Unit){
    navigate(dest.dest, navOptions(builder))
}

fun NavController.navigate(dest: Destination, param: String, builder: NavOptionsBuilder.() -> Unit){
    navigate("${dest.dest}/$param", navOptions(builder))
}

fun NavController.navigate(dest: Destination){
    navigate(dest.dest)
}

fun NavController.navigate(dest: Destination, param: String){
    navigate("${dest.dest}/$param")
}