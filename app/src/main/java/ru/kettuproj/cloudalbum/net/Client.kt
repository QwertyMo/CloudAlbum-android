package ru.kettuproj.cloudalbum.net

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import ru.kettuproj.cloudalbum.model.RepositoryResponse
import java.io.IOException

object Client {
    @OptIn(ExperimentalSerializationApi::class)
    val client = HttpClient(OkHttp) {
        install(HttpTimeout) {
            requestTimeoutMillis = 15000
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint       = true
                isLenient         = true
                explicitNulls     = true
            })
        }
    }
}

suspend inline fun<reified T> get(
    request: String,
    onErrorData: T,
    httpBuilder: HttpRequestBuilder.() -> Unit = {}
): RepositoryResponse<T> {
    return try{
        val data = Client.client.get(request, httpBuilder)
        if(data.status == HttpStatusCode.OK)
            RepositoryResponse(data.status, data.body() as T)
        else RepositoryResponse(data.status, onErrorData)
    }catch (e: IOException){
        RepositoryResponse(HttpStatusCode.NotFound, onErrorData)
    }
}

suspend inline fun<reified T> post(
    request: String,
    onErrorData: T,
    httpBuilder: HttpRequestBuilder.() -> Unit = {}
): RepositoryResponse<T> {
    return try{
        val data = Client.client.post(request, httpBuilder)
        if(data.status == HttpStatusCode.OK)
            RepositoryResponse(data.status, data.body() as T)
        else RepositoryResponse(data.status, onErrorData)
    }catch (e: IOException){
        RepositoryResponse(HttpStatusCode.NotFound, onErrorData)
    }
}

suspend inline fun<reified T> delete(
    request: String,
    onErrorData: T,
    httpBuilder: HttpRequestBuilder.() -> Unit = {}
): RepositoryResponse<T> {
    return try{
        val data = Client.client.delete(request, httpBuilder)
        if(data.status == HttpStatusCode.OK) {
            RepositoryResponse(data.status, data.body() as T)

        }
        else RepositoryResponse(data.status, onErrorData)
    }catch (e: IOException){
        RepositoryResponse(HttpStatusCode.NotFound, onErrorData)
    }
}

suspend fun delete(
    request: String,
    httpBuilder: HttpRequestBuilder.() -> Unit = {}
): RepositoryResponse<EmptyData> {
    return try{
        val data = Client.client.delete(request, httpBuilder)
        RepositoryResponse(data.status, EmptyData())
    }catch (e: IOException){
        RepositoryResponse(HttpStatusCode.NotFound, EmptyData())
    }
}

suspend fun post(
    request: String,
    httpBuilder: HttpRequestBuilder.() -> Unit = {}
): RepositoryResponse<EmptyData> {
    return try{
        val data = Client.client.post(request, httpBuilder)
        RepositoryResponse(data.status, EmptyData())
    }catch (e: IOException){
        RepositoryResponse(HttpStatusCode.NotFound, EmptyData())
    }
}

suspend fun get(
    request: String,
    httpBuilder: HttpRequestBuilder.() -> Unit = {}
): RepositoryResponse<EmptyData> {
    return try{
        val data = Client.client.get(request, httpBuilder)
        RepositoryResponse(data.status, EmptyData())
    }catch (e: IOException){
        RepositoryResponse(HttpStatusCode.NotFound, EmptyData())
    }
}

@kotlinx.serialization.Serializable
class EmptyData