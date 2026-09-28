package com.slashgil.marvel.data.repository

import com.slashgil.marvel.data.mapper.toDomain
import com.slashgil.marvel.data.remote.api.MarvelApi
import com.slashgil.marvel.domain.model.Character
import com.slashgil.marvel.domain.model.Comic
import com.slashgil.marvel.domain.repository.MarvelRepository
import javax.inject.Inject

class MarvelRepositoryImpl @Inject constructor(
    private val api: MarvelApi
) : MarvelRepository {

    override suspend fun getCharacters(
        query: String?,
        limit: Int,
        offset: Int
    ): Result<List<Character>> {
        return runCatching {
            val response = api.getCharacters(
                nameStartsWith = query?.takeIf { it.isNotBlank() },
                limit = limit,
                offset = offset
            )
            response.data?.results?.map { it.toDomain() } ?: emptyList()
        }
    }

    override suspend fun getCharacterDetails(characterId: Long): Result<Character> {
        return runCatching {
            val response = api.getCharacterDetails(characterId)
            val dto = response.data?.results?.firstOrNull()
                ?: throw NoSuchElementException("Character with ID $characterId not found")
            dto.toDomain()
        }
    }

    override suspend fun getComicsForCharacter(
        characterId: Long,
        limit: Int,
        offset: Int
    ): Result<List<Comic>> {
        return runCatching {
            val response = api.getComicsForCharacter(
                characterId = characterId,
                limit = limit,
                offset = offset
            )
            response.data?.results?.map { it.toDomain() } ?: emptyList()
        }
    }
}
