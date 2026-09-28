package com.slashgil.marvel.domain.usecase

import com.slashgil.marvel.domain.model.Character

interface GetCharacterDetailsUseCase {
    suspend operator fun invoke(characterId: String): Result<Character>
}
