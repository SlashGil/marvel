package com.slashgil.marvel.domain.repository

import com.slashgil.marvel.domain.model.Character
import com.slashgil.marvel.domain.model.Comic

interface MarvelRepository {
    suspend fun getCharacters(query: String? = null, limit: Int = 20, offset: Int = 0): Result<List<Character>>
    suspend fun getCharacterDetails(characterId: Long): Result<Character>
    suspend fun getComicsForCharacter(characterId: Long, limit: Int = 20, offset: Int = 0): Result<List<Comic>>
}
