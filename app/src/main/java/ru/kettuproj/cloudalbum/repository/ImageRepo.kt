package ru.kettuproj.cloudalbum.repository

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import ru.kettuproj.cloudalbum.common.Constant.REST_HOST
import ru.kettuproj.cloudalbum.model.*
import ru.kettuproj.stocks.net.Client

object ImageRepo {

    suspend fun getMyImages(token: String): RepositoryResponse<List<Image>>{
        val data = Client.client.get("$REST_HOST/image/my"){
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

}