package com.slashgil.marvel.data.local.mapper

import com.slashgil.marvel.data.local.entity.CharacterEntity
import com.slashgil.marvel.domain.model.Appearance
import com.slashgil.marvel.domain.model.Biography
import com.slashgil.marvel.domain.model.Character
import com.slashgil.marvel.domain.model.Connections
import com.slashgil.marvel.domain.model.Powerstats
import com.slashgil.marvel.domain.model.Work

fun CharacterEntity.toDomain(): Character {
    return Character(
        id = id,
        name = name,
        imageUrl = imageUrl,
        powerstats = Powerstats(
            intelligence = intelligence,
            strength = strength,
            speed = speed,
            durability = durability,
            power = power,
            combat = combat
        ),
        biography = Biography(
            fullName = fullName,
            alterEgos = alterEgos,
            aliases = if (aliases.isBlank()) emptyList() else aliases.split(", "),
            placeOfBirth = placeOfBirth,
            firstAppearance = firstAppearance,
            publisher = publisher,
            alignment = alignment
        ),
        appearance = Appearance(
            gender = gender,
            race = race,
            height = height,
            weight = weight,
            eyeColor = eyeColor,
            hairColor = hairColor
        ),
        work = Work(
            occupation = occupation,
            base = base
        ),
        connections = Connections(
            groupAffiliation = groupAffiliation,
            relatives = relatives
        )
    )
}

fun Character.toEntity(): CharacterEntity {
    return CharacterEntity(
        id = id,
        name = name,
        imageUrl = imageUrl,
        intelligence = powerstats.intelligence,
        strength = powerstats.strength,
        speed = powerstats.speed,
        durability = powerstats.durability,
        power = powerstats.power,
        combat = powerstats.combat,
        fullName = biography.fullName,
        alterEgos = biography.alterEgos,
        aliases = biography.aliases.joinToString(", "),
        placeOfBirth = biography.placeOfBirth,
        firstAppearance = biography.firstAppearance,
        publisher = biography.publisher,
        alignment = biography.alignment,
        gender = appearance.gender,
        race = appearance.race,
        height = appearance.height,
        weight = appearance.weight,
        eyeColor = appearance.eyeColor,
        hairColor = appearance.hairColor,
        occupation = work.occupation,
        base = work.base,
        groupAffiliation = connections.groupAffiliation,
        relatives = connections.relatives
    )
}
