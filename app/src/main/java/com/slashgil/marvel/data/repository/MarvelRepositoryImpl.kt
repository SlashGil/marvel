package com.slashgil.marvel.data.repository

import com.slashgil.marvel.data.mapper.toDomain
import com.slashgil.marvel.data.remote.api.MarvelApi
import com.slashgil.marvel.domain.model.Appearance
import com.slashgil.marvel.domain.model.Biography
import com.slashgil.marvel.domain.model.Character
import com.slashgil.marvel.domain.model.Comic
import com.slashgil.marvel.domain.model.Connections
import com.slashgil.marvel.domain.model.Powerstats
import com.slashgil.marvel.domain.model.Work
import com.slashgil.marvel.domain.repository.MarvelRepository
import javax.inject.Inject

class MarvelRepositoryImpl @Inject constructor(
    private val api: MarvelApi
) : MarvelRepository {

    override suspend fun getCharacters(
        query: String?,
        publisher: String?
    ): Result<List<Character>> {
        return runCatching {
            val q = query?.trim().orEmpty().ifEmpty { "a" }
            val response = api.searchCharacters(q)
            val networkList = response.results?.map { it.toDomain() } ?: emptyList()

            val combined = if (networkList.isNotEmpty()) networkList else getFallbackCharacters(query)
            filterByPublisher(combined, publisher)
        }.fold(
            onSuccess = { Result.success(it) },
            onFailure = {
                val fallback = filterByPublisher(getFallbackCharacters(query), publisher)
                Result.success(fallback)
            }
        )
    }

    override suspend fun getCharacterDetails(characterId: String): Result<Character> {
        return runCatching {
            val dto = api.getCharacterDetails(characterId)
            if (dto.id != null) {
                dto.toDomain()
            } else {
                sampleCharacters.find { it.id == characterId } ?: sampleCharacters.first()
            }
        }.fold(
            onSuccess = { Result.success(it) },
            onFailure = {
                val fallback = sampleCharacters.find { c -> c.id == characterId } ?: sampleCharacters.first()
                Result.success(fallback)
            }
        )
    }

    override suspend fun getComicsForCharacter(characterId: String): Result<List<Comic>> {
        val character = sampleCharacters.find { it.id == characterId }
        val comics = if (character != null) {
            listOf(
                Comic(
                    id = "${characterId}_1",
                    title = character.biography.firstAppearance.ifBlank { "First Appearance #${character.name}" },
                    description = "First debut issue featuring ${character.name}",
                    thumbnailUrl = character.imageUrl,
                    publisher = character.biography.publisher
                )
            )
        } else {
            sampleComics
        }
        return Result.success(comics)
    }

    private fun filterByPublisher(list: List<Character>, publisher: String?): List<Character> {
        val pub = publisher?.trim().orEmpty()
        if (pub.isBlank() || pub.equals("All", ignoreCase = true)) return list
        return list.filter { it.biography.publisher.contains(pub, ignoreCase = true) }
    }

    private fun getFallbackCharacters(query: String?): List<Character> {
        val q = query?.trim().orEmpty()
        return if (q.isNotBlank()) {
            sampleCharacters.filter {
                it.name.contains(q, ignoreCase = true) ||
                it.biography.fullName.contains(q, ignoreCase = true) ||
                it.biography.publisher.contains(q, ignoreCase = true)
            }
        } else {
            sampleCharacters
        }
    }

    companion object {
        val sampleCharacters = listOf(
            Character(
                id = "620",
                name = "Spider-Man",
                imageUrl = "https://www.superherodb.com/pictures2/portraits/10/100/133.jpg",
                powerstats = Powerstats(intelligence = 90, strength = 55, speed = 67, durability = 75, power = 74, combat = 85),
                biography = Biography(
                    fullName = "Peter Parker",
                    alterEgos = "No alter egos found.",
                    aliases = listOf("Spidey", "Wall-Crawler", "Web-Slinger"),
                    placeOfBirth = "New York, New York",
                    firstAppearance = "Amazing Fantasy #15",
                    publisher = "Marvel Comics",
                    alignment = "good"
                ),
                appearance = Appearance(gender = "Male", race = "Human", height = "5'10, 178 cm", weight = "165 lb, 74 kg", eyeColor = "Hazel", hairColor = "Brown"),
                work = Work(occupation = "Photographer, Scientist", base = "New York, New York"),
                connections = Connections(groupAffiliation = "Avengers, Fantastic Four", relatives = "May Parker (aunt), Ben Parker (uncle, deceased)")
            ),
            Character(
                id = "346",
                name = "Iron Man",
                imageUrl = "https://www.superherodb.com/pictures2/portraits/10/100/85.jpg",
                powerstats = Powerstats(intelligence = 100, strength = 85, speed = 58, durability = 85, power = 100, combat = 64),
                biography = Biography(
                    fullName = "Tony Stark",
                    alterEgos = "No alter egos found.",
                    aliases = listOf("Golden Avenger", "Armored Avenger"),
                    placeOfBirth = "Long Island, New York",
                    firstAppearance = "Tales of Suspense #39",
                    publisher = "Marvel Comics",
                    alignment = "good"
                ),
                appearance = Appearance(gender = "Male", race = "Human", height = "6'1, 185 cm", weight = "225 lb, 102 kg", eyeColor = "Blue", hairColor = "Black"),
                work = Work(occupation = "Inventor, Businessman", base = "Stark Tower, New York"),
                connections = Connections(groupAffiliation = "Avengers, Illuminati", relatives = "Howard Stark (father, deceased), Maria Stark (mother, deceased)")
            ),
            Character(
                id = "70",
                name = "Batman",
                imageUrl = "https://www.superherodb.com/pictures2/portraits/10/100/639.jpg",
                powerstats = Powerstats(intelligence = 100, strength = 26, speed = 27, durability = 50, power = 47, combat = 100),
                biography = Biography(
                    fullName = "Bruce Wayne",
                    alterEgos = "No alter egos found.",
                    aliases = listOf("Dark Knight", "Caped Crusader"),
                    placeOfBirth = "Gotham City",
                    firstAppearance = "Detective Comics #27",
                    publisher = "DC Comics",
                    alignment = "good"
                ),
                appearance = Appearance(gender = "Male", race = "Human", height = "6'2, 188 cm", weight = "210 lb, 95 kg", eyeColor = "Blue", hairColor = "Black"),
                work = Work(occupation = "CEO of Wayne Enterprises", base = "Batcave, Gotham City"),
                connections = Connections(groupAffiliation = "Justice League, Batman Family", relatives = "Thomas Wayne (father, deceased), Martha Wayne (mother, deceased)")
            ),
            Character(
                id = "644",
                name = "Superman",
                imageUrl = "https://www.superherodb.com/pictures2/portraits/10/100/791.jpg",
                powerstats = Powerstats(intelligence = 94, strength = 100, speed = 100, durability = 100, power = 100, combat = 85),
                biography = Biography(
                    fullName = "Clark Kent (Kal-El)",
                    alterEgos = "No alter egos found.",
                    aliases = listOf("Man of Steel", "Son of Krypton"),
                    placeOfBirth = "Krypton",
                    firstAppearance = "Action Comics #1",
                    publisher = "DC Comics",
                    alignment = "good"
                ),
                appearance = Appearance(gender = "Male", race = "Kryptonian", height = "6'3, 191 cm", weight = "235 lb, 107 kg", eyeColor = "Blue", hairColor = "Black"),
                work = Work(occupation = "Reporter for Daily Planet", base = "Metropolis, Fortress of Solitude"),
                connections = Connections(groupAffiliation = "Justice League", relatives = "Jor-El (father, deceased), Lara Lor-Van (mother, deceased)")
            ),
            Character(
                id = "149",
                name = "Captain America",
                imageUrl = "https://www.superherodb.com/pictures2/portraits/10/100/274.jpg",
                powerstats = Powerstats(intelligence = 69, strength = 19, speed = 38, durability = 55, power = 60, combat = 100),
                biography = Biography(
                    fullName = "Steve Rogers",
                    alterEgos = "No alter egos found.",
                    aliases = listOf("Cap", "First Avenger"),
                    placeOfBirth = "Manhattan, New York",
                    firstAppearance = "Captain America Comics #1",
                    publisher = "Marvel Comics",
                    alignment = "good"
                ),
                appearance = Appearance(gender = "Male", race = "Human", height = "6'2, 188 cm", weight = "220 lb, 99 kg", eyeColor = "Blue", hairColor = "Blond"),
                work = Work(occupation = "Adventurer, Soldier", base = "New York, New York"),
                connections = Connections(groupAffiliation = "Avengers, Invaders", relatives = "Joseph Rogers (father, deceased)")
            ),
            Character(
                id = "659",
                name = "Thor",
                imageUrl = "https://www.superherodb.com/pictures2/portraits/10/100/140.jpg",
                powerstats = Powerstats(intelligence = 69, strength = 100, speed = 83, durability = 100, power = 100, combat = 100),
                biography = Biography(
                    fullName = "Thor Odinson",
                    alterEgos = "No alter egos found.",
                    aliases = listOf("God of Thunder", "Son of Odin"),
                    placeOfBirth = "Asgard",
                    firstAppearance = "Journey into Mystery #83",
                    publisher = "Marvel Comics",
                    alignment = "good"
                ),
                appearance = Appearance(gender = "Male", race = "Asgardian", height = "6'6, 198 cm", weight = "640 lb, 288 kg", eyeColor = "Blue", hairColor = "Blond"),
                work = Work(occupation = "King of Asgard, Avenger", base = "Asgard, New York"),
                connections = Connections(groupAffiliation = "Avengers, Gods of Asgard", relatives = "Odin (father), Frigga (mother), Loki (adopted brother)")
            )
        )

        val sampleComics = listOf(
            Comic(
                id = "1",
                title = "Amazing Fantasy #15",
                description = "First appearance of Spider-Man!",
                thumbnailUrl = "https://www.superherodb.com/pictures2/portraits/10/100/133.jpg",
                publisher = "Marvel Comics"
            ),
            Comic(
                id = "2",
                title = "Detective Comics #27",
                description = "First appearance of Batman!",
                thumbnailUrl = "https://www.superherodb.com/pictures2/portraits/10/100/639.jpg",
                publisher = "DC Comics"
            ),
            Comic(
                id = "3",
                title = "Action Comics #1",
                description = "First appearance of Superman!",
                thumbnailUrl = "https://www.superherodb.com/pictures2/portraits/10/100/791.jpg",
                publisher = "DC Comics"
            )
        )
    }
}
