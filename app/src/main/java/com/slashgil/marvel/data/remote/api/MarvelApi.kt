package com.slashgil.marvel.data.remote.api

import com.slashgil.marvel.data.remote.dto.SuperheroDto
import com.slashgil.marvel.data.remote.dto.SuperheroSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface MarvelApi {

    @GET("api/$ACCESS_TOKEN/search/{name}")
    suspend fun searchCharacters(
        @Path("name") name: String
    ): SuperheroSearchResponseDto

    @GET("api/$ACCESS_TOKEN/{id}")
    suspend fun getCharacterDetails(
        @Path("id") id: String
    ): SuperheroDto

    companion object {
        const val ACCESS_TOKEN = "c2242a8ba90dc3677b35cc19860bce66"
    }
}
