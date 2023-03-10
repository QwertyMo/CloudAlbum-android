package ru.kettuproj.cloudalbum.repository

import io.ktor.client.request.*
import ru.kettuproj.cloudalbum.common.Constant
import ru.kettuproj.cloudalbum.model.RepositoryResponse
import ru.kettuproj.cloudalbum.model.Token
import ru.kettuproj.cloudalbum.model.User
import ru.kettuproj.cloudalbum.net.GET
import ru.kettuproj.cloudalbum.net.POST

object AuthRepo {
    suspend fun postLogin(login: String, pass: String): RepositoryResponse<Token?> {
        return POST("${Constant.REST_HOST}/login", null){
            parameter("login", login)
            parameter("password", pass)
        }
    }

    suspend fun getMe(token: String):RepositoryResponse<User?>{
        return GET("${Constant.REST_HOST}/user/me", null){
            bearerAuth(token)
        }
    }
}