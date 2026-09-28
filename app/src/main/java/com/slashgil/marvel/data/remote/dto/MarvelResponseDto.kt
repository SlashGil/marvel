package com.slashgil.marvel.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MarvelResponseDto<T>(
    val code: Int? = null,
    val status: String? = null,
    val data: MarvelDataContainerDto<T>? = null
)

@Serializable
data class MarvelDataContainerDto<T>(
    val offset: Int? = null,
    val limit: Int? = null,
    val total: Int? = null,
    val count: Int? = null,
    val results: List<T> = emptyList()
)

@Serializable
data class ThumbnailDto(
    val path: String? = null,
    val extension: String? = null
) {
    val fullUrl: String
        get() = if (!path.isNullOrEmpty() && !extension.isNullOrEmpty()) {
            "$path.$extension".replace("http://", "https://")
        } else {
            ""
        }
}
