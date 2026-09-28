package com.slashgil.marvel.domain.model

data class Comic(
    val id: String,
    val title: String,
    val description: String? = null,
    val thumbnailUrl: String = "",
    val publisher: String = "Marvel Comics"
)
