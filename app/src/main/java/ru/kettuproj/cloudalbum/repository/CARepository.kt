package ru.kettuproj.cloudalbum.repository

import android.util.Log
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import ru.kettuproj.cloudalbum.common.Constant.REST_HOST
import ru.kettuproj.cloudalbum.model.*
import ru.kettuproj.stocks.net.Client
import java.io.File
import java.util.UUID

object CARepository {

    suspend fun postLogin(login: String, pass: String): RepositoryResponse<Token?>{
        val data = Client.client.post("$REST_HOST/login"){
            parameter("login", login)
            parameter("password", pass)
        }
        return if(data.status == HttpStatusCode.OK)
            RepositoryResponse(data.status, data.body())
        else RepositoryResponse(data.status, null)
    }

    suspend fun createAlbum(token: String, name: String): RepositoryResponse<Album?>{
        val data = Client.client.post("$REST_HOST/album"){
            bearerAuth(token)
            parameter("name", name)
        }
        return if(data.status == HttpStatusCode.OK)
            RepositoryResponse(data.status, data.body())

        else RepositoryResponse(data.status, null)
    }

    suspend fun getMyImages(token: String): RepositoryResponse<List<Image>>{
        val data = Client.client.get("$REST_HOST/image/my"){
            bearerAuth(token)
        }
        return if(data.status == HttpStatusCode.OK)
            RepositoryResponse(data.status, data.body())

        else RepositoryResponse(data.status, listOf())
    }

    suspend fun getAlbumImages(token: String, albumID: Int): RepositoryResponse<List<Image>>{
        val data = Client.client.get("$REST_HOST/image/album"){
            bearerAuth(token)
            parameter("albumID", albumID)
        }
        return if(data.status == HttpStatusCode.OK)
            RepositoryResponse(data.status, data.body())

        else RepositoryResponse(data.status, listOf())
    }

    suspend fun getMyAlbums(token: String): RepositoryResponse<List<Album>>{
        val data = Client.client.get("$REST_HOST/album/my"){
            bearerAuth(token)
        }
        return if(data.status == HttpStatusCode.OK)
            RepositoryResponse(data.status, data.body())
        else RepositoryResponse(data.status, listOf())
    }

    suspend fun getImageByUUID(token: String, uuid: String): RepositoryResponse<Image?>{
        val data = Client.client.get("$REST_HOST/image"){
            bearerAuth(token)
            parameter("uuid", uuid)
        }
        return if(data.status == HttpStatusCode.OK)
            RepositoryResponse(data.status, data.body())

        else RepositoryResponse(data.status, null)
    }

    suspend fun uploadImage(token: String, img: ByteArray, id: Int? = null): RepositoryResponse<Image?>{
        val data = Client.client.post("$REST_HOST/image"){
            bearerAuth(token)
            if(id!=null) parameter("albumID", id)
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("",img, Headers.build {
                            append(HttpHeaders.ContentType, "image/*")
                            append(HttpHeaders.ContentDisposition, "filename=\"img\"")
                        })
                    }
                )
            )
        }
        return if(data.status == HttpStatusCode.OK)
            RepositoryResponse(data.status, data.body())

        else RepositoryResponse(data.status, null)
    }

    suspend fun deleteImage(token: String, uuid: String): RepositoryResponse<Boolean>{
        val data = Client.client.delete("$REST_HOST/image"){
            bearerAuth(token)
            parameter("uuid", uuid)
        }
        return if(data.status == HttpStatusCode.OK)
            RepositoryResponse(data.status, true)
        else RepositoryResponse(data.status, false)
    }

    suspend fun getMe(token: String):RepositoryResponse<User?>{
        val data = Client.client.get("$REST_HOST/user/me"){
            bearerAuth(token)
        }
        return if(data.status == HttpStatusCode.OK)
            RepositoryResponse(data.status, data.body())

        else RepositoryResponse(data.status, null)
    }

}