package com.slashgil.marvel.presentation.characters

import com.slashgil.marvel.domain.model.Character
import com.slashgil.marvel.domain.usecase.FakeMarvelRepository
import com.slashgil.marvel.domain.usecase.GetCharacterDetailsUseCaseImpl
import com.slashgil.marvel.domain.usecase.GetCharactersUseCaseImpl
import com.slashgil.marvel.domain.usecase.GetComicsForCharacterUseCaseImpl
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
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharactersViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val fakeRepository = FakeMarvelRepository()

    private val getCharactersUseCase = GetCharactersUseCaseImpl(fakeRepository)
    private val getCharacterDetailsUseCase = GetCharacterDetailsUseCaseImpl(fakeRepository)
    private val getComicsForCharacterUseCase = GetComicsForCharacterUseCaseImpl(fakeRepository)

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
                id = "620",
                name = "Spider-Man",
                imageUrl = "http://example.com/spiderman.jpg"
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
    fun `onPublisherSelected filters character list`() = runTest {
        val mockCharacters = listOf(
            Character(
                id = "620",
                name = "Spider-Man",
                imageUrl = "http://example.com/spiderman.jpg"
            )
        )
        fakeRepository.charactersResult = Result.success(mockCharacters)

        viewModel = CharactersViewModel(
            getCharactersUseCase,
            getCharacterDetailsUseCase,
            getComicsForCharacterUseCase
        )
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onPublisherSelected("DC Comics")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("DC Comics", state.selectedPublisher)
    }
}
