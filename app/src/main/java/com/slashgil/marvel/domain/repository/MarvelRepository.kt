package com.slashgil.marvel.domain.repository

import com.slashgil.marvel.domain.model.Character
import com.slashgil.marvel.domain.model.Comic

interface MarvelRepository {
    suspend fun getCharacters(query: String? = null, publisher: String? = null): Result<List<Character>>
    suspend fun getCharacterDetails(characterId: String): Result<Character>
    suspend fun getComicsForCharacter(characterId: String): Result<List<Comic>>
}
