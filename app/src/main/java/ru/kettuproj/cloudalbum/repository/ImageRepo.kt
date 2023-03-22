package ru.kettuproj.cloudalbum.repository

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import ru.kettuproj.cloudalbum.common.Constant.REST_HOST
import ru.kettuproj.cloudalbum.model.*
import ru.kettuproj.cloudalbum.net.EmptyData
import ru.kettuproj.cloudalbum.net.delete
import ru.kettuproj.cloudalbum.net.get
import ru.kettuproj.cloudalbum.net.post

object ImageRepo {

    suspend fun getMyImages(token: String): RepositoryResponse<List<Image>>{
        return get("$REST_HOST/image/my", listOf()){
            bearerAuth(token)
        }
    }

    suspend fun getImageByUUID(token: String, uuid: String): RepositoryResponse<Image?>{
        return get("$REST_HOST/image", null){
            bearerAuth(token)
            parameter("uuid", uuid)
        }
    }

    suspend fun uploadImage(token: String, img: ByteArray, id: Int? = null): RepositoryResponse<Image?>{
        return post("$REST_HOST/image", null) {
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

    suspend fun getPosedImage(token: String, pos: Int, albumID: Int? = null): RepositoryResponse<Image?>{
        val data = get<List<Image>>("$REST_HOST/image/posed", emptyList()){
            bearerAuth(token)
            if (albumID != null) parameter("albumID", albumID)
            parameter("posStart", pos)
            parameter("posEnd", pos)
        }
        return if(data.data.size==1) RepositoryResponse(data.status, data.data[0])
        else RepositoryResponse(data.status, null)
    }

    suspend fun deleteImage(token: String, uuid: String): RepositoryResponse<EmptyData>{
        return delete("$REST_HOST/image"){
            bearerAuth(token)
            parameter("uuid", uuid)
        }
    }

    suspend fun getDataCount(token: String, albumID: Int? = null): RepositoryResponse<DataCount?>{
        return get("$REST_HOST/image/count", null){
            bearerAuth(token)
            if (albumID != null) parameter("albumID", albumID)
        }
    }

}