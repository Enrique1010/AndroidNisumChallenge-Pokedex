package com.erapps.pokedexapp.data.api.models.encounters

data class VersionDetail(
    val encounter_details: List<EncounterDetail>,
    val max_chance: Int,
    val version: Version
)