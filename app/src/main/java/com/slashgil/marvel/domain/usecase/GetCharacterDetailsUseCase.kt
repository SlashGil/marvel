package com.slashgil.marvel.domain.usecase

import com.slashgil.marvel.domain.model.Character
import com.slashgil.marvel.domain.repository.MarvelRepository
import javax.inject.Inject

class GetCharacterDetailsUseCase @Inject constructor(
    private val repository: MarvelRepository
) {
    suspend operator fun invoke(characterId: Long): Result<Character> {
        return repository.getCharacterDetails(characterId)
    }
}
