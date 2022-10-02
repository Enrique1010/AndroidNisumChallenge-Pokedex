package com.erapps.pokedexapp.data.api.models.encounters.locationarea

data class PokemonEncounter(
    val pokemon: Pokemon,
    val version_details: List<VersionDetailX>
)