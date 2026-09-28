package com.slashgil.marvel.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CharacterDto(
    val id: Long? = null,
    val name: String? = null,
    val description: String? = null,
    val thumbnail: ThumbnailDto? = null,
    val comics: ResourceListDto? = null,
    val series: ResourceListDto? = null
)

@Serializable
data class ResourceListDto(
    val available: Int? = null,
    val collectionURI: String? = null
)
