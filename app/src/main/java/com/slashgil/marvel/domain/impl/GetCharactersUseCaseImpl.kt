package com.slashgil.marvel.domain.impl

import com.slashgil.marvel.domain.contract.GetCharactersUseCase
import com.slashgil.marvel.data.repository.contract.MarvelRepositoryContract
import com.slashgil.marvel.domain.model.Character
import javax.inject.Inject

class GetCharactersUseCaseImpl @Inject constructor(
    private val repository: MarvelRepositoryContract
) : GetCharactersUseCase {
    override suspend fun invoke(
        query: String?,
        publisher: String?
    ): Result<List<Character>> {
        return repository.getCharacters(query = query, publisher = publisher)
    }
}
