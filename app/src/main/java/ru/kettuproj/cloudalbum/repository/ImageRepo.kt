package ru.kettuproj.cloudalbum.repository

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import ru.kettuproj.cloudalbum.common.Constant.REST_HOST
import ru.kettuproj.cloudalbum.model.*
import ru.kettuproj.cloudalbum.net.DELETE
import ru.kettuproj.cloudalbum.net.GET
import ru.kettuproj.cloudalbum.net.POST

object ImageRepo {

    suspend fun getMyImages(token: String): RepositoryResponse<List<Image>>{
        return GET("$REST_HOST/image/my", listOf()){
            bearerAuth(token)
        }
    }

    suspend fun getImageByUUID(token: String, uuid: String): RepositoryResponse<Image?>{
        return GET("$REST_HOST/image", null){
            bearerAuth(token)
            parameter("uuid", uuid)
        }
    }

    suspend fun uploadImage(token: String, img: ByteArray, id: Int? = null): RepositoryResponse<Image?>{
        return POST("$REST_HOST/image", null) {
            bearerAuth(token)
            if (id != null) parameter("albumID", id)
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("", img, Headers.build {
                            append(HttpHeaders.ContentType, "image/*")
                            append(HttpHeaders.ContentDisposition, "filename=\"img\"")
                        })
                    }
                )
            )
        }
    }

    suspend fun deleteImage(token: String, uuid: String): RepositoryResponse<Boolean>{
        return DELETE("$REST_HOST/image", false){
            bearerAuth(token)
            parameter("uuid", uuid)
        }
    }

}