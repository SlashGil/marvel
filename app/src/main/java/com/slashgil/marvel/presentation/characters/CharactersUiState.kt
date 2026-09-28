package com.slashgil.marvel.presentation.characters

import com.slashgil.marvel.domain.model.Character
import com.slashgil.marvel.domain.model.Comic

data class CharactersUiState(
    val isLoading: Boolean = false,
    val characters: List<Character> = emptyList(),
    val searchQuery: String = "",
    val error: String? = null,
    val selectedCharacter: Character? = null,
    val isDetailLoading: Boolean = false,
    val characterComics: List<Comic> = emptyList()
)
