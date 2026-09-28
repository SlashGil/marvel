package com.slashgil.marvel.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SuperheroSearchResponseDto(
    val response: String? = null,
    @SerialName("results-for") val resultsFor: String? = null,
    val results: List<SuperheroDto>? = null,
    val error: String? = null
)

@Serializable
data class SuperheroDto(
    val id: String? = null,
    val name: String? = null,
    val powerstats: PowerstatsDto? = null,
    val biography: BiographyDto? = null,
    val appearance: AppearanceDto? = null,
    val work: WorkDto? = null,
    val connections: ConnectionsDto? = null,
    val image: ImageDto? = null
)

@Serializable
data class PowerstatsDto(
    val intelligence: String? = null,
    val strength: String? = null,
    val speed: String? = null,
    val durability: String? = null,
    val power: String? = null,
    val combat: String? = null
)

@Serializable
data class BiographyDto(
    @SerialName("full-name") val fullName: String? = null,
    @SerialName("alter-egos") val alterEgos: String? = null,
    val aliases: List<String>? = null,
    @SerialName("place-of-birth") val placeOfBirth: String? = null,
    @SerialName("first-appearance") val firstAppearance: String? = null,
    val publisher: String? = null,
    val alignment: String? = null
)

@Serializable
data class AppearanceDto(
    val gender: String? = null,
    val race: String? = null,
    val height: List<String>? = null,
    val weight: List<String>? = null,
    @SerialName("eye-color") val eyeColor: String? = null,
    @SerialName("hair-color") val hairColor: String? = null
)

@Serializable
data class WorkDto(
    val occupation: String? = null,
    val base: String? = null
)

@Serializable
data class ConnectionsDto(
    @SerialName("group-affiliation") val groupAffiliation: String? = null,
    val relatives: String? = null
)

@Serializable
data class ImageDto(
    val url: String? = null
)
