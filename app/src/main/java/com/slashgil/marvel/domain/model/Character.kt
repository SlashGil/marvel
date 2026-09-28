package com.slashgil.marvel.domain.model

data class Character(
    val id: Long,
    val name: String,
    val description: String,
    val thumbnailUrl: String,
    val comicsAvailable: Int,
    val seriesAvailable: Int
)
