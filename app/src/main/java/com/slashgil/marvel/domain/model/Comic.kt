package com.slashgil.marvel.domain.model

data class Comic(
    val id: Long,
    val title: String,
    val description: String?,
    val thumbnailUrl: String,
    val pageCount: Int
)
