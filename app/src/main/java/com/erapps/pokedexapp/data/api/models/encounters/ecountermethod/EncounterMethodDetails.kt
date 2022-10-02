package com.erapps.pokedexapp.data.api.models.encounters.ecountermethod

data class EncounterMethodDetails(
    val id: Int,
    val name: String,
    val names: List<Name>,
    val order: Int
)