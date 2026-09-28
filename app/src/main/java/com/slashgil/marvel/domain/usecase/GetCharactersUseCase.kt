package com.slashgil.marvel.domain.usecase

import com.slashgil.marvel.domain.model.Character
import com.slashgil.marvel.domain.repository.MarvelRepository
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val repository: MarvelRepository
) {
    suspend operator fun invoke(
        query: String? = null,
        limit: Int = 20,
        offset: Int = 0
    ): Result<List<Character>> {
        return repository.getCharacters(query = query, limit = limit, offset = offset)
    }
}
