package ru.kettuproj.cloudalbum.model

import io.ktor.http.*

data class RepositoryResponse <out A>(
    val status: HttpStatusCode,
    val data  : A
)