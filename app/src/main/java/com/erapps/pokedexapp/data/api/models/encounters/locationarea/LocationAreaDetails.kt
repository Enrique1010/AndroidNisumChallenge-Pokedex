package com.erapps.pokedexapp.data.api.models.encounters.locationarea

data class LocationAreaDetails(
    val encounter_method_rates: List<EncounterMethodRate>,
    val game_index: Int,
    val id: Int,
    val location: Location,
    val name: String,
    val names: List<Name>,
    val pokemon_encounters: List<PokemonEncounter>
)