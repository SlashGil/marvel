package com.slashgil.marvel.data.repository

import com.slashgil.marvel.data.remote.api.MarvelApi
import com.slashgil.marvel.data.remote.dto.CharacterDto
import com.slashgil.marvel.data.remote.dto.ComicDto
import com.slashgil.marvel.data.remote.dto.MarvelDataContainerDto
import com.slashgil.marvel.data.remote.dto.MarvelResponseDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MarvelRepositoryImplTest {

    private val api: MarvelApi = mockk()
    private val repository = MarvelRepositoryImpl(api)

    @Test
    fun `getCharacters returns mapped domain models when api call succeeds`() = runTest {
        val characterDto = CharacterDto(
            id = 1009610,
            name = "Spider-Man",
            description = "Friendly neighborhood Spider-Man"
        )
        val response = MarvelResponseDto(
            code = 200,
            status = "Ok",
            data = MarvelDataContainerDto(results = listOf(characterDto))
        )
        coEvery { api.getCharacters(nameStartsWith = "Spider", limit = 20, offset = 0) } returns response

        val result = repository.getCharacters(query = "Spider", limit = 20, offset = 0)

        assertTrue(result.isSuccess)
        val characters = result.getOrNull()
        assertEquals(1, characters?.size)
        assertEquals("Spider-Man", characters?.first()?.name)
        coVerify(exactly = 1) { api.getCharacters(nameStartsWith = "Spider", limit = 20, offset = 0) }
    }

    @Test
    fun `getCharacters returns failure when api throws exception`() = runTest {
        coEvery { api.getCharacters(any(), any(), any()) } throws RuntimeException("Server error 500")

        val result = repository.getCharacters(query = null, limit = 20, offset = 0)

        assertTrue(result.isFailure)
        assertEquals("Server error 500", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getCharacterDetails returns character domain model when found`() = runTest {
        val characterDto = CharacterDto(
            id = 1009610,
            name = "Spider-Man",
            description = "Friendly neighborhood Spider-Man"
        )
        val response = MarvelResponseDto(
            code = 200,
            status = "Ok",
            data = MarvelDataContainerDto(results = listOf(characterDto))
        )
        coEvery { api.getCharacterDetails(1009610) } returns response

        val result = repository.getCharacterDetails(1009610)

        assertTrue(result.isSuccess)
        assertEquals("Spider-Man", result.getOrNull()?.name)
    }

    @Test
    fun `getComicsForCharacter returns mapped comics when api call succeeds`() = runTest {
        val comicDto = ComicDto(
            id = 1,
            title = "Amazing Spider-Man #1",
            description = "Classic issue"
        )
        val response = MarvelResponseDto(
            code = 200,
            status = "Ok",
            data = MarvelDataContainerDto(results = listOf(comicDto))
        )
        coEvery { api.getComicsForCharacter(1009610, 20, 0) } returns response

        val result = repository.getComicsForCharacter(1009610, 20, 0)

        assertTrue(result.isSuccess)
        assertEquals("Amazing Spider-Man #1", result.getOrNull()?.first()?.title)
    }
}
