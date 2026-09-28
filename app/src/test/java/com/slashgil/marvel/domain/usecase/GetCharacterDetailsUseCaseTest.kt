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
            id = "620",
            name = "Spider-Man",
            imageUrl = "http://example.com/spiderman.jpg"
        )
        fakeRepository.characterDetailsResult = Result.success(mockCharacter)

        val result = useCase("620")

        assertTrue(result.isSuccess)
        assertEquals(mockCharacter, result.getOrNull())
    }
}
