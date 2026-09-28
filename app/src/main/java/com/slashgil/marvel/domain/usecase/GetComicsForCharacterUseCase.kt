package com.slashgil.marvel.domain.usecase

import com.slashgil.marvel.domain.model.Comic
import com.slashgil.marvel.domain.repository.MarvelRepository
import javax.inject.Inject

class GetComicsForCharacterUseCase @Inject constructor(
    private val repository: MarvelRepository
) {
    suspend operator fun invoke(
        characterId: Long,
        limit: Int = 20,
        offset: Int = 0
    ): Result<List<Comic>> {
        return repository.getComicsForCharacter(characterId = characterId, limit = limit, offset = offset)
    }
}
