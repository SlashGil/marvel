package com.slashgil.marvel.domain.usecase

import com.slashgil.marvel.domain.model.Comic
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetComicsForCharacterUseCaseTest {

    private val fakeRepository = FakeMarvelRepository()
    private val useCase = GetComicsForCharacterUseCase(fakeRepository)

    @Test
    fun `invoke returns comics list when repository succeeds`() = runTest {
        val mockComics = listOf(
            Comic(
                id = 1,
                title = "Amazing Spider-Man #1",
                description = "First issue",
                thumbnailUrl = "http://i.annihil.us/u/prod/marvel/i/mg/1/10/1.jpg",
                pageCount = 32
            )
        )
        fakeRepository.comicsResult = Result.success(mockComics)

        val result = useCase(characterId = 1009610, limit = 20, offset = 0)

        assertTrue(result.isSuccess)
        assertEquals(mockComics, result.getOrNull())
    }
}
