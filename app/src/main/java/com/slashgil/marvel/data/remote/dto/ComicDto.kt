package com.slashgil.marvel.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ComicDto(
    val id: Long? = null,
    val title: String? = null,
    val description: String? = null,
    val thumbnail: ThumbnailDto? = null,
    val pageCount: Int? = null
)
