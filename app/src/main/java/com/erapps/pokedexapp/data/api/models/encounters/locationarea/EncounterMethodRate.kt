package com.erapps.pokedexapp.data.api.models.encounters.locationarea

data class EncounterMethodRate(
    val encounter_method: EncounterMethod,
    val version_details: List<VersionDetail>
)