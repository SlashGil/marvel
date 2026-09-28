package com.slashgil.marvel.presentation.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slashgil.marvel.domain.model.Character
import com.slashgil.marvel.domain.usecase.GetCharacterDetailsUseCase
import com.slashgil.marvel.domain.usecase.GetCharactersUseCase
import com.slashgil.marvel.domain.usecase.GetComicsForCharacterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val getCharacterDetailsUseCase: GetCharacterDetailsUseCase,
    private val getComicsForCharacterUseCase: GetComicsForCharacterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharactersUiState())
    val uiState: StateFlow<CharactersUiState> = _uiState.asStateFlow()

    private val searchQueryFlow = MutableStateFlow("")

    init {
        loadCharacters()

        viewModelScope.launch {
            searchQueryFlow
                .debounce(300)
                .collect { query ->
                    fetchCharacters(query)
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchQueryFlow.value = query
    }

    fun loadCharacters() {
        fetchCharacters(_uiState.value.searchQuery)
    }

    private fun fetchCharacters(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = getCharactersUseCase(query = query)
            result.fold(
                onSuccess = { list ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            characters = list,
                            error = if (list.isEmpty()) "No Marvel characters found" else null
                        )
                    }
                },
                onFailure = { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.localizedMessage ?: "An unexpected error occurred"
                        )
                    }
                }
            )
        }
    }

    fun onCharacterSelected(character: Character) {
        _uiState.update {
            it.copy(
                selectedCharacter = character,
                isDetailLoading = true,
                characterComics = emptyList()
            )
        }
        viewModelScope.launch {
            val detailsResult = getCharacterDetailsUseCase(character.id)
            val comicsResult = getComicsForCharacterUseCase(character.id)

            val updatedCharacter = detailsResult.getOrDefault(character)
            val comicsList = comicsResult.getOrDefault(emptyList())

            _uiState.update {
                it.copy(
                    selectedCharacter = updatedCharacter,
                    characterComics = comicsList,
                    isDetailLoading = false
                )
            }
        }
    }

    fun onDismissDetail() {
        _uiState.update {
            it.copy(
                selectedCharacter = null,
                isDetailLoading = false,
                characterComics = emptyList()
            )
        }
    }
}
