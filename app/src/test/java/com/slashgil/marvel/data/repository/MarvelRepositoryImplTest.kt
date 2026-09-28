package com.slashgil.marvel.data.repository

import com.slashgil.marvel.data.datasource.remote.MarvelRemoteDataSource
import com.slashgil.marvel.data.remote.dto.ImageDto
import com.slashgil.marvel.data.remote.dto.SuperheroDto
import com.slashgil.marvel.data.remote.dto.SuperheroSearchResponseDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MarvelRepositoryImplTest {

    private val remoteDataSource: MarvelRemoteDataSource = mockk()
    private val repository = MarvelRepositoryImpl(remoteDataSource)

    @Test
    fun `getCharacters returns mapped domain models when api call succeeds`() = runTest {
        val heroDto = SuperheroDto(
            id = "620",
            name = "Spider-Man",
            image = ImageDto(url = "http://example.com/spiderman.jpg")
        )
        val response = SuperheroSearchResponseDto(
            response = "success",
            results = listOf(heroDto)
        )
        coEvery { remoteDataSource.searchCharacters("Spider") } returns response

        val result = repository.getCharacters(query = "Spider", publisher = null)

        assertTrue(result.isSuccess)
        val characters = result.getOrNull()
        assertEquals("Spider-Man", characters?.first()?.name)
        coVerify(exactly = 1) { remoteDataSource.searchCharacters("Spider") }
    }

    @Test
    fun `getCharacterDetails returns character domain model when found`() = runTest {
        val heroDto = SuperheroDto(
            id = "620",
            name = "Spider-Man",
            image = ImageDto(url = "http://example.com/spiderman.jpg")
        )
        coEvery { remoteDataSource.getCharacterDetails("620") } returns heroDto

        val result = repository.getCharacterDetails("620")

        assertTrue(result.isSuccess)
        assertEquals("Spider-Man", result.getOrNull()?.name)
    }
}
