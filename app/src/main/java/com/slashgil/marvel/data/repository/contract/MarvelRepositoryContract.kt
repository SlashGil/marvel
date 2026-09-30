package com.slashgil.marvel.data.repository.contract

import com.slashgil.marvel.domain.model.Character

interface MarvelRepositoryContract {
    suspend fun getCharacters(
        query: String? = null,
        publisher: String? = null
    ): Result<List<Character>>

    suspend fun getCharacterDetails(characterId: String): Result<Character>
}
