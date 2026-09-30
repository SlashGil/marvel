package com.slashgil.marvel.data.remote.contract

import com.slashgil.marvel.data.remote.dto.SuperheroDto
import com.slashgil.marvel.data.remote.dto.SuperheroSearchResponseDto

interface MarvelRemoteDataSource {
    suspend fun searchCharacters(name: String): SuperheroSearchResponseDto
    suspend fun getCharacterDetails(id: String): SuperheroDto
}
