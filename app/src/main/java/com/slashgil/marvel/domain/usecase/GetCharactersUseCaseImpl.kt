package com.slashgil.marvel.domain.usecase

import com.slashgil.marvel.domain.model.Character
import com.slashgil.marvel.domain.repository.MarvelRepository
import javax.inject.Inject

class GetCharactersUseCaseImpl @Inject constructor(
    private val repository: MarvelRepository
) : GetCharactersUseCase {
    override suspend fun invoke(
        query: String?,
        publisher: String?
    ): Result<List<Character>> {
        return repository.getCharacters(query = query, publisher = publisher)
    }
}
