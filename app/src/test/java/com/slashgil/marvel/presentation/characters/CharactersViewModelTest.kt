package com.slashgil.marvel.presentation.characters

import com.slashgil.marvel.domain.model.Character
import com.slashgil.marvel.domain.model.Comic
import com.slashgil.marvel.domain.usecase.FakeMarvelRepository
import com.slashgil.marvel.domain.usecase.GetCharacterDetailsUseCase
import com.slashgil.marvel.domain.usecase.GetCharactersUseCase
import com.slashgil.marvel.domain.usecase.GetComicsForCharacterUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharactersViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val fakeRepository = FakeMarvelRepository()

    private val getCharactersUseCase = GetCharactersUseCase(fakeRepository)
    private val getCharacterDetailsUseCase = GetCharacterDetailsUseCase(fakeRepository)
    private val getComicsForCharacterUseCase = GetComicsForCharacterUseCase(fakeRepository)

    private lateinit var viewModel: CharactersViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init loads characters successfully`() = runTest {
        val mockCharacters = listOf(
            Character(
                id = 1009610,
                name = "Spider-Man",
                description = "Hero",
                thumbnailUrl = "http://example.com/spiderman.jpg",
                comicsAvailable = 10,
                seriesAvailable = 5
            )
        )
        fakeRepository.charactersResult = Result.success(mockCharacters)

        viewModel = CharactersViewModel(
            getCharactersUseCase,
            getCharacterDetailsUseCase,
            getComicsForCharacterUseCase
        )

        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertEquals(mockCharacters, state.characters)
    }

    @Test
    fun `onCharacterSelected fetches details and comics`() = runTest {
        val mockCharacter = Character(
            id = 1009610,
            name = "Spider-Man",
            description = "Friendly neighborhood Spider-Man",
            thumbnailUrl = "http://example.com/spiderman.jpg",
            comicsAvailable = 10,
            seriesAvailable = 5
        )
        val mockComics = listOf(
            Comic(
                id = 1,
                title = "Amazing Spider-Man #1",
                description = "Issue 1",
                thumbnailUrl = "http://example.com/comic1.jpg",
                pageCount = 32
            )
        )
        fakeRepository.charactersResult = Result.success(listOf(mockCharacter))
        fakeRepository.characterDetailsResult = Result.success(mockCharacter)
        fakeRepository.comicsResult = Result.success(mockComics)

        viewModel = CharactersViewModel(
            getCharactersUseCase,
            getCharacterDetailsUseCase,
            getComicsForCharacterUseCase
        )
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onCharacterSelected(mockCharacter)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(mockCharacter, state.selectedCharacter)
        assertEquals(mockComics, state.characterComics)
        assertFalse(state.isDetailLoading)
    }

    @Test
    fun `onDismissDetail clears selected character and comics`() = runTest {
        val mockCharacter = Character(
            id = 1009610,
            name = "Spider-Man",
            description = "Hero",
            thumbnailUrl = "http://example.com/spiderman.jpg",
            comicsAvailable = 10,
            seriesAvailable = 5
        )
        fakeRepository.characterDetailsResult = Result.success(mockCharacter)

        viewModel = CharactersViewModel(
            getCharactersUseCase,
            getCharacterDetailsUseCase,
            getComicsForCharacterUseCase
        )
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onCharacterSelected(mockCharacter)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onDismissDetail()

        val state = viewModel.uiState.value
        assertNull(state.selectedCharacter)
        assertTrue(state.characterComics.isEmpty())
    }
}
