package com.slashgil.marvel.data.mapper

import com.slashgil.marvel.data.remote.dto.SuperheroDto
import com.slashgil.marvel.domain.model.Appearance
import com.slashgil.marvel.domain.model.Biography
import com.slashgil.marvel.domain.model.Character
import com.slashgil.marvel.domain.model.Connections
import com.slashgil.marvel.domain.model.Powerstats
import com.slashgil.marvel.domain.model.Work

fun SuperheroDto.toDomain(): Character {
    val charId = id.orEmpty()
    val charName = name.orEmpty()
    return Character(
        id = charId,
        name = charName,
        imageUrl = resolveImageUrl(charId, charName, image?.url, size = "md"),
        powerstats = Powerstats(
            intelligence = powerstats?.intelligence?.toIntOrNull() ?: 0,
            strength = powerstats?.strength?.toIntOrNull() ?: 0,
            speed = powerstats?.speed?.toIntOrNull() ?: 0,
            durability = powerstats?.durability?.toIntOrNull() ?: 0,
            power = powerstats?.power?.toIntOrNull() ?: 0,
            combat = powerstats?.combat?.toIntOrNull() ?: 0
        ),
        biography = Biography(
            fullName = biography?.fullName.orEmpty(),
            alterEgos = biography?.alterEgos.orEmpty(),
            aliases = biography?.aliases ?: emptyList(),
            placeOfBirth = biography?.placeOfBirth.orEmpty(),
            firstAppearance = biography?.firstAppearance.orEmpty(),
            publisher = biography?.publisher ?: "Marvel Comics",
            alignment = biography?.alignment ?: "good"
        ),
        appearance = Appearance(
            gender = appearance?.gender.orEmpty(),
            race = appearance?.race.orEmpty(),
            height = appearance?.height?.joinToString(", ").orEmpty(),
            weight = appearance?.weight?.joinToString(", ").orEmpty(),
            eyeColor = appearance?.eyeColor.orEmpty(),
            hairColor = appearance?.hairColor.orEmpty()
        ),
        work = Work(
            occupation = work?.occupation.orEmpty(),
            base = work?.base.orEmpty()
        ),
        connections = Connections(
            groupAffiliation = connections?.groupAffiliation.orEmpty(),
            relatives = connections?.relatives.orEmpty()
        )
    )
}

fun resolveImageUrl(id: String, name: String, rawUrl: String?, size: String = "md"): String {
    val cleanUrl = rawUrl?.trim().orEmpty()
    if (cleanUrl.contains("superherodb.com", ignoreCase = true) || cleanUrl.isBlank()) {
        val slug = name.lowercase()
            .replace(" ", "-")
            .replace(Regex("[^a-z0-9-]"), "")
            .trim('-')
        if (id.isNotBlank() && slug.isNotBlank()) {
            return "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/$size/$id-$slug.jpg"
        }
    }
    return cleanUrl.replace("http://", "https://")
}
