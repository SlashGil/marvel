package com.slashgil.marvel.presentation.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slashgil.marvel.domain.model.Character
import com.slashgil.marvel.domain.model.Comic
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
                    val charactersList = if (list.isNotEmpty()) list else getFallbackCharacters(query)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            characters = charactersList,
                            error = null
                        )
                    }
                },
                onFailure = { throwable ->
                    val fallbackList = getFallbackCharacters(query)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            characters = fallbackList,
                            error = if (fallbackList.isEmpty()) throwable.localizedMessage else null
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
            val comicsList = comicsResult.getOrDefault(sampleComics)

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

    companion object {
        val sampleCharacters = listOf(
            Character(
                id = 1009610,
                name = "Spider-Man",
                description = "Bitten by a radioactive spider, Peter Parker's arachnid abilities give him amazing powers he uses to help others.",
                thumbnailUrl = "https://i.annihil.us/u/prod/marvel/i/mg/3/50/526548a343e4b.jpg",
                comicsAvailable = 4000,
                seriesAvailable = 1000
            ),
            Character(
                id = 1009368,
                name = "Iron Man",
                description = "Wounded, captured and forced to build a weapon by his enemies, billionaire industrialist Tony Stark instead created an advanced suit of armor to save his life and escape captivity.",
                thumbnailUrl = "https://i.annihil.us/u/prod/marvel/i/mg/9/c0/527bb7b37ff55.jpg",
                comicsAvailable = 2600,
                seriesAvailable = 650
            ),
            Character(
                id = 1009220,
                name = "Captain America",
                description = "Vowing to serve his country in any way he could, young Steve Rogers took the super-soldier serum to become America's one-man army.",
                thumbnailUrl = "https://i.annihil.us/u/prod/marvel/i/mg/3/50/537ba61d3b0fe.jpg",
                comicsAvailable = 2400,
                seriesAvailable = 600
            ),
            Character(
                id = 1009664,
                name = "Thor",
                description = "As the Norse God of Thunder and Lightning, Thor wields one of the greatest weapons ever made, the enchanted hammer Mjolnir.",
                thumbnailUrl = "https://i.annihil.us/u/prod/marvel/i/mg/d/d0/5269657a74350.jpg",
                comicsAvailable = 1800,
                seriesAvailable = 450
            ),
            Character(
                id = 1009718,
                name = "Wolverine",
                description = "A mutant with an unstoppable healing factor, adamantium-plated skeleton and retractable claws, Wolverine is a lethal member of the X-Men.",
                thumbnailUrl = "https://i.annihil.us/u/prod/marvel/i/mg/2/60/537bca7032027.jpg",
                comicsAvailable = 2200,
                seriesAvailable = 550
            ),
            Character(
                id = 1009189,
                name = "Black Widow",
                description = "Natasha Romanoff is one of the world's greatest spies and a master of martial arts.",
                thumbnailUrl = "https://i.annihil.us/u/prod/marvel/i/mg/f/30/50fe4c08b6128.jpg",
                comicsAvailable = 1100,
                seriesAvailable = 300
            )
        )

        val sampleComics = listOf(
            Comic(
                id = 1,
                title = "The Amazing Spider-Man #1",
                description = "The origin of Spider-Man continues!",
                thumbnailUrl = "https://i.annihil.us/u/prod/marvel/i/mg/1/10/1.jpg",
                pageCount = 32
            ),
            Comic(
                id = 2,
                title = "Civil War #1",
                description = "Whose side are you on?",
                thumbnailUrl = "https://i.annihil.us/u/prod/marvel/i/mg/2/20/2.jpg",
                pageCount = 48
            ),
            Comic(
                id = 3,
                title = "Infinity Gauntlet #1",
                description = "Thanos gathers the infinity stones.",
                thumbnailUrl = "https://i.annihil.us/u/prod/marvel/i/mg/3/30/3.jpg",
                pageCount = 40
            )
        )

        fun getFallbackCharacters(query: String?): List<Character> {
            val q = query?.trim().orEmpty()
            return if (q.isNotBlank()) {
                sampleCharacters.filter { it.name.contains(q, ignoreCase = true) }
            } else {
                sampleCharacters
            }
        }
    }
}
