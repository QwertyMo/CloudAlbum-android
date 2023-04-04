package ru.kettuproj.cloudalbum.repository

import io.ktor.client.request.*
import ru.kettuproj.cloudalbum.common.Constant
import ru.kettuproj.cloudalbum.model.Album
import ru.kettuproj.cloudalbum.model.Image
import ru.kettuproj.cloudalbum.model.RepositoryResponse
import ru.kettuproj.cloudalbum.net.get
import ru.kettuproj.cloudalbum.net.post

object AlbumRepo {
    suspend fun createAlbum(token: String, name: String): RepositoryResponse<Album?> {
        return post("${Constant.REST_HOST}/album", null){
            bearerAuth(token)
            parameter("name", name)
        }
    }

    suspend fun getAlbumImages(token: String, albumID: Int): RepositoryResponse<List<Image>> {
        return get("${Constant.REST_HOST}/image/album", listOf()){
            bearerAuth(token)
            parameter("albumID", albumID)
        }
    }

    suspend fun getAlbum(token: String, albumID: Int): RepositoryResponse<Album?> {
        return get("${Constant.REST_HOST}/album", null){
            bearerAuth(token)
            parameter("albumID", albumID)
        }
    }

    suspend fun getMyAlbums(token: String): RepositoryResponse<List<Album>> {
        return get("${Constant.REST_HOST}/album/my", listOf()){
            bearerAuth(token)
        }
    }
}