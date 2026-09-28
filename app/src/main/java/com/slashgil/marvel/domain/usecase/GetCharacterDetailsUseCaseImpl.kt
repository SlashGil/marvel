package com.slashgil.marvel.domain.usecase

import com.slashgil.marvel.domain.model.Character
import com.slashgil.marvel.domain.repository.MarvelRepository
import javax.inject.Inject

class GetCharacterDetailsUseCaseImpl @Inject constructor(
    private val repository: MarvelRepository
) : GetCharacterDetailsUseCase {
    override suspend fun invoke(characterId: String): Result<Character> {
        return repository.getCharacterDetails(characterId)
    }
}
