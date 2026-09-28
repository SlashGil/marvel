package com.slashgil.marvel.domain.usecase

import com.slashgil.marvel.domain.model.Comic

interface GetComicsForCharacterUseCase {
    suspend operator fun invoke(characterId: String): Result<List<Comic>>
}
