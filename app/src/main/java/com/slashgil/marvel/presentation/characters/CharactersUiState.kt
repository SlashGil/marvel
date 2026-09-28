package com.slashgil.marvel.presentation.characters

import com.slashgil.marvel.domain.model.Character
import com.slashgil.marvel.domain.model.Comic

data class CharactersUiState(
    val isLoading: Boolean = false,
    val characters: List<Character> = emptyList(),
    val searchQuery: String = "",
    val selectedPublisher: String = "All",
    val availablePublishers: List<String> = listOf("All", "Marvel Comics", "DC Comics", "Dark Horse Comics", "George Lucas"),
    val error: String? = null,
    val selectedCharacter: Character? = null,
    val isDetailLoading: Boolean = false,
    val characterComics: List<Comic> = emptyList()
)
