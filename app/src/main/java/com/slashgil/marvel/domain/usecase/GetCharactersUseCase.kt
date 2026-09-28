package com.slashgil.marvel.domain.usecase

import com.slashgil.marvel.domain.model.Character

interface GetCharactersUseCase {
    suspend operator fun invoke(
        query: String? = null,
        publisher: String? = null
    ): Result<List<Character>>
}
