package com.slashgil.marvel.domain.impl

import com.slashgil.marvel.domain.contract.GetCharacterDetailsUseCase
import com.slashgil.marvel.data.repository.contract.MarvelRepositoryContract
import com.slashgil.marvel.domain.model.Character
import javax.inject.Inject

class GetCharacterDetailsUseCaseImpl @Inject constructor(
    private val repository: MarvelRepositoryContract
) : GetCharacterDetailsUseCase {
    override suspend fun invoke(characterId: String): Result<Character> {
        return repository.getCharacterDetails(characterId)
    }
}
