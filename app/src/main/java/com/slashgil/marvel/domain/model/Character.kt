package com.slashgil.marvel.domain.model

data class Character(
    val id: String,
    val name: String,
    val imageUrl: String,
    val powerstats: Powerstats = Powerstats(),
    val biography: Biography = Biography(),
    val appearance: Appearance = Appearance(),
    val work: Work = Work(),
    val connections: Connections = Connections()
)

data class Powerstats(
    val intelligence: Int = 0,
    val strength: Int = 0,
    val speed: Int = 0,
    val durability: Int = 0,
    val power: Int = 0,
    val combat: Int = 0
)

data class Biography(
    val fullName: String = "",
    val alterEgos: String = "",
    val aliases: List<String> = emptyList(),
    val placeOfBirth: String = "",
    val firstAppearance: String = "",
    val publisher: String = "Marvel Comics",
    val alignment: String = "good"
)

data class Appearance(
    val gender: String = "",
    val race: String = "",
    val height: String = "",
    val weight: String = "",
    val eyeColor: String = "",
    val hairColor: String = ""
)

data class Work(
    val occupation: String = "",
    val base: String = ""
)

data class Connections(
    val groupAffiliation: String = "",
    val relatives: String = ""
)
