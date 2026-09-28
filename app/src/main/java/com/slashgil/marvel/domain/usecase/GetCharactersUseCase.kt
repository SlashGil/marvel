package com.slashgil.marvel.domain.usecase

import com.slashgil.marvel.domain.model.Character
import com.slashgil.marvel.domain.repository.MarvelRepository
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val repository: MarvelRepository
) {
    suspend operator fun invoke(
        query: String? = null,
        publisher: String? = null
    ): Result<List<Character>> {
        return repository.getCharacters(query = query, publisher = publisher)
    }
}
