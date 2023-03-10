package ru.kettuproj.cloudalbum.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.kettuproj.cloudalbum.model.Image
import ru.kettuproj.cloudalbum.model.RepositoryResponse
import ru.kettuproj.cloudalbum.net.Client
import java.io.IOException
import java.net.InetAddress

fun checkIsInternetAvailable(context: Context): Boolean {
    var result = false
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
    }
    return result
}

suspend fun checkIsHostAvailable():Boolean{
    return try {
        Client.client.request(Constant.REST_HOST)
        true
    }catch (e: java.lang.Exception){
        false
    }
}