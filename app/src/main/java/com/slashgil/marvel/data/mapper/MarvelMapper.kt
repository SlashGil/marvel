package com.slashgil.marvel.data.mapper

import com.slashgil.marvel.data.remote.dto.CharacterDto
import com.slashgil.marvel.data.remote.dto.ComicDto
import com.slashgil.marvel.domain.model.Character
import com.slashgil.marvel.domain.model.Comic

fun CharacterDto.toDomain(): Character {
    return Character(
        id = id ?: 0L,
        name = name.orEmpty(),
        description = description.orEmpty(),
        thumbnailUrl = thumbnail?.fullUrl.orEmpty(),
        comicsAvailable = comics?.available ?: 0,
        seriesAvailable = series?.available ?: 0
    )
}

fun ComicDto.toDomain(): Comic {
    return Comic(
        id = id ?: 0L,
        title = title.orEmpty(),
        description = description,
        thumbnailUrl = thumbnail?.fullUrl.orEmpty(),
        pageCount = pageCount ?: 0
    )
}
