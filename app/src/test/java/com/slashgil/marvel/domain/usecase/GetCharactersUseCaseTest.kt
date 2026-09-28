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
    private val useCase = GetCharactersUseCase(fakeRepository)

    @Test
    fun `invoke returns success result when repository succeeds`() = runTest {
        val mockCharacters = listOf(
            Character(
                id = 1009610,
                name = "Spider-Man",
                description = "Bitten by a radioactive spider...",
                thumbnailUrl = "http://i.annihil.us/u/prod/marvel/i/mg/3/50/526548a343e4b.jpg",
                comicsAvailable = 4000,
                seriesAvailable = 1000
            )
        )
        fakeRepository.charactersResult = Result.success(mockCharacters)

        val result = useCase(query = "Spider", limit = 20, offset = 0)

        assertTrue(result.isSuccess)
        assertEquals(mockCharacters, result.getOrNull())
    }

    @Test
    fun `invoke returns failure result when repository fails`() = runTest {
        val exception = RuntimeException("Network error")
        fakeRepository.charactersResult = Result.failure(exception)

        val result = useCase(query = null, limit = 20, offset = 0)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}

class FakeMarvelRepository : MarvelRepository {
    var charactersResult: Result<List<Character>> = Result.success(emptyList())
    var characterDetailsResult: Result<Character>? = null
    var comicsResult: Result<List<Comic>> = Result.success(emptyList())

    override suspend fun getCharacters(query: String?, limit: Int, offset: Int): Result<List<Character>> {
        return charactersResult
    }

    override suspend fun getCharacterDetails(characterId: Long): Result<Character> {
        return characterDetailsResult ?: Result.failure(NoSuchElementException())
    }

    override suspend fun getComicsForCharacter(characterId: Long, limit: Int, offset: Int): Result<List<Comic>> {
        return comicsResult
    }
}
