package com.slashgil.marvel.data.local.contract

import com.slashgil.marvel.domain.model.Character

interface MarvelLocalDataSource {
    suspend fun searchCharacters(query: String, publisher: String): List<Character>
    suspend fun getCharacterById(id: String): Character?
    suspend fun saveCharacters(characters: List<Character>)
    suspend fun getCharacterCount(): Int
}
