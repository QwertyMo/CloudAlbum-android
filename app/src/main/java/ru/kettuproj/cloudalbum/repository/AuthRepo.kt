package ru.kettuproj.cloudalbum.repository

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.kettuproj.cloudalbum.common.Constant
import ru.kettuproj.cloudalbum.model.RepositoryResponse
import ru.kettuproj.cloudalbum.model.Token
import ru.kettuproj.cloudalbum.model.User
import ru.kettuproj.stocks.net.Client

object AuthRepo {
    suspend fun postLogin(login: String, pass: String): RepositoryResponse<Token?> {
        val data = Client.client.post("${Constant.REST_HOST}/login"){
            parameter("login", login)
            parameter("password", pass)
        }
        return if(data.status == HttpStatusCode.OK)
            RepositoryResponse(data.status, data.body())
        else RepositoryResponse(data.status, null)
    }

    suspend fun getMe(token: String):RepositoryResponse<User?>{
        val data = Client.client.get("${Constant.REST_HOST}/user/me"){
            bearerAuth(token)
        }
        return if(data.status == HttpStatusCode.OK)
            RepositoryResponse(data.status, data.body())

        else RepositoryResponse(data.status, null)
    }
}