package com.slashgil.marvel.domain.usecase

import com.slashgil.marvel.domain.model.Comic
import com.slashgil.marvel.domain.repository.MarvelRepository
import javax.inject.Inject

class GetComicsForCharacterUseCaseImpl @Inject constructor(
    private val repository: MarvelRepository
) : GetComicsForCharacterUseCase {
    override suspend fun invoke(characterId: String): Result<List<Comic>> {
        return repository.getComicsForCharacter(characterId)
    }
}
