package ru.kettuproj.cloudalbum.repository

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.kettuproj.cloudalbum.common.Constant
import ru.kettuproj.cloudalbum.model.Album
import ru.kettuproj.cloudalbum.model.Image
import ru.kettuproj.cloudalbum.model.RepositoryResponse
import ru.kettuproj.stocks.net.Client

object AlbumRepo {
    suspend fun createAlbum(token: String, name: String): RepositoryResponse<Album?> {
        val data = Client.client.post("${Constant.REST_HOST}/album"){
            bearerAuth(token)
            parameter("name", name)
        }
        return if(data.status == HttpStatusCode.OK)
            RepositoryResponse(data.status, data.body())

        else RepositoryResponse(data.status, null)
    }

    suspend fun getAlbumImages(token: String, albumID: Int): RepositoryResponse<List<Image>> {
        val data = Client.client.get("${Constant.REST_HOST}/image/album"){
            bearerAuth(token)
            parameter("albumID", albumID)
        }
        return if(data.status == HttpStatusCode.OK)
            RepositoryResponse(data.status, data.body())

        else RepositoryResponse(data.status, listOf())
    }

    suspend fun getMyAlbums(token: String): RepositoryResponse<List<Album>> {
        val data = Client.client.get("${Constant.REST_HOST}/album/my"){
            bearerAuth(token)
        }
        return if(data.status == HttpStatusCode.OK)
            RepositoryResponse(data.status, data.body())
        else RepositoryResponse(data.status, listOf())
    }
}