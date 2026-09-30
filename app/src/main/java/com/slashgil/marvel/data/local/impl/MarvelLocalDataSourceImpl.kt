package com.slashgil.marvel.data.local.impl

import com.slashgil.marvel.data.local.contract.MarvelLocalDataSource
import com.slashgil.marvel.data.local.dao.CharacterDao
import com.slashgil.marvel.data.local.mapper.toDomain
import com.slashgil.marvel.data.local.mapper.toEntity
import com.slashgil.marvel.domain.model.Character
import javax.inject.Inject

class MarvelLocalDataSourceImpl @Inject constructor(
    private val characterDao: CharacterDao
) : MarvelLocalDataSource {

    override suspend fun searchCharacters(query: String, publisher: String): List<Character> {
        return characterDao.searchCharacters(query, publisher).map { it.toDomain() }
    }

    override suspend fun getCharacterById(id: String): Character? {
        return characterDao.getCharacterById(id)?.toDomain()
    }

    override suspend fun saveCharacters(characters: List<Character>) {
        if (characters.isNotEmpty()) {
            characterDao.insertCharacters(characters.map { it.toEntity() })
        }
    }

    override suspend fun getCharacterCount(): Int {
        return characterDao.getCharacterCount()
    }
}
