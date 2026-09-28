package com.slashgil.marvel.data.remote.api

import com.slashgil.marvel.data.remote.dto.SuperheroDto
import com.slashgil.marvel.data.remote.dto.SuperheroSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface MarvelApi {

    @GET("api.php/10222018804928373/search/{name}")
    suspend fun searchCharacters(
        @Path("name") name: String
    ): SuperheroSearchResponseDto

    @GET("api.php/10222018804928373/{id}")
    suspend fun getCharacterDetails(
        @Path("id") id: String
    ): SuperheroDto
}
