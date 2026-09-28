package com.slashgil.marvel.data.remote.api

import com.slashgil.marvel.data.remote.dto.CharacterDto
import com.slashgil.marvel.data.remote.dto.ComicDto
import com.slashgil.marvel.data.remote.dto.MarvelResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelApi {

    @GET("characters")
    suspend fun getCharacters(
        @Query("nameStartsWith") nameStartsWith: String? = null,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): MarvelResponseDto<CharacterDto>

    @GET("characters/{characterId}")
    suspend fun getCharacterDetails(
        @Path("characterId") characterId: Long
    ): MarvelResponseDto<CharacterDto>

    @GET("characters/{characterId}/comics")
    suspend fun getComicsForCharacter(
        @Path("characterId") characterId: Long,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): MarvelResponseDto<ComicDto>
}
