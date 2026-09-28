package com.slashgil.marvel.domain.usecase

import com.slashgil.marvel.domain.model.Character
import com.slashgil.marvel.domain.model.Comic
import com.slashgil.marvel.domain.repository.MarvelRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetCharactersUseCaseTest {

    private val fakeRepository = FakeMarvelRepository()
    private val useCase: GetCharactersUseCase = GetCharactersUseCaseImpl(fakeRepository)

    @Test
    fun `invoke returns success result when repository succeeds`() = runTest {
        val mockCharacters = listOf(
            Character(
                id = "620",
                name = "Spider-Man",
                imageUrl = "http://example.com/spiderman.jpg"
            )
        )
        fakeRepository.charactersResult = Result.success(mockCharacters)

        val result = useCase(query = "Spider", publisher = "Marvel Comics")

        assertTrue(result.isSuccess)
        assertEquals(mockCharacters, result.getOrNull())
    }

    @Test
    fun `invoke returns failure result when repository fails`() = runTest {
        val exception = RuntimeException("Network error")
        fakeRepository.charactersResult = Result.failure(exception)

        val result = useCase(query = null, publisher = null)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}

class FakeMarvelRepository : MarvelRepository {
    var charactersResult: Result<List<Character>> = Result.success(emptyList())
    var characterDetailsResult: Result<Character>? = null
    var comicsResult: Result<List<Comic>> = Result.success(emptyList())

    override suspend fun getCharacters(query: String?, publisher: String?): Result<List<Character>> {
        return charactersResult
    }

    override suspend fun getCharacterDetails(characterId: String): Result<Character> {
        return characterDetailsResult ?: Result.failure(NoSuchElementException())
    }

    override suspend fun getComicsForCharacter(characterId: String): Result<List<Comic>> {
        return comicsResult
    }
}
