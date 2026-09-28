package com.slashgil.marvel.data.datasource.remote

import com.slashgil.marvel.data.remote.api.MarvelApi
import com.slashgil.marvel.data.remote.dto.SuperheroDto
import com.slashgil.marvel.data.remote.dto.SuperheroSearchResponseDto
import javax.inject.Inject

class MarvelRemoteDataSourceImpl @Inject constructor(
    private val api: MarvelApi
) : MarvelRemoteDataSource {

    override suspend fun searchCharacters(name: String): SuperheroSearchResponseDto {
        return api.searchCharacters(name)
    }

    override suspend fun getCharacterDetails(id: String): SuperheroDto {
        return api.getCharacterDetails(id)
    }
}
