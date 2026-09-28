package com.slashgil.marvel.domain.usecase

import com.slashgil.marvel.domain.model.Comic
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetComicsForCharacterUseCaseTest {

    private val fakeRepository = FakeMarvelRepository()
    private val useCase: GetComicsForCharacterUseCase = GetComicsForCharacterUseCaseImpl(fakeRepository)

    @Test
    fun `invoke returns comics list when repository succeeds`() = runTest {
        val mockComics = listOf(
            Comic(
                id = "1",
                title = "Amazing Spider-Man #1",
                description = "First issue",
                thumbnailUrl = "http://example.com/comic1.jpg"
            )
        )
        fakeRepository.comicsResult = Result.success(mockComics)

        val result = useCase("620")

        assertTrue(result.isSuccess)
        assertEquals(mockComics, result.getOrNull())
    }
}
