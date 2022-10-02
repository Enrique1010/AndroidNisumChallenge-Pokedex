package com.erapps.pokedexapp.data.api.models.encounters.locationarea

data class VersionDetailX(
    val encounter_details: List<EncounterDetail>,
    val max_chance: Int,
    val version: Version
)