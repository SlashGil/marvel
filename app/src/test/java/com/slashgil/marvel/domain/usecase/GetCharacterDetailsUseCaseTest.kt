package com.slashgil.marvel.domain.usecase

import com.slashgil.marvel.domain.model.Character
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetCharacterDetailsUseCaseTest {

    private val fakeRepository = FakeMarvelRepository()
    private val useCase = GetCharacterDetailsUseCase(fakeRepository)

    @Test
    fun `invoke returns character details when repository succeeds`() = runTest {
        val mockCharacter = Character(
            id = 1009610,
            name = "Spider-Man",
            description = "Friendly neighborhood Spider-Man",
            thumbnailUrl = "http://i.annihil.us/u/prod/marvel/i/mg/3/50/526548a343e4b.jpg",
            comicsAvailable = 4000,
            seriesAvailable = 1000
        )
        fakeRepository.characterDetailsResult = Result.success(mockCharacter)

        val result = useCase(1009610)

        assertTrue(result.isSuccess)
        assertEquals(mockCharacter, result.getOrNull())
    }
}
