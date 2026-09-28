package com.slashgil.marvel.data.mapper

import com.slashgil.marvel.data.remote.dto.SuperheroDto
import com.slashgil.marvel.domain.model.Appearance
import com.slashgil.marvel.domain.model.Biography
import com.slashgil.marvel.domain.model.Character
import com.slashgil.marvel.domain.model.Connections
import com.slashgil.marvel.domain.model.Powerstats
import com.slashgil.marvel.domain.model.Work

fun SuperheroDto.toDomain(): Character {
    return Character(
        id = id.orEmpty(),
        name = name.orEmpty(),
        imageUrl = image?.url.orEmpty(),
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
