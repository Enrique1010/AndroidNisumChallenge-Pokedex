package com.erapps.pokedexapp.data.api.models

data class PokemonListResponse(
    val count: Int,
    val next: String,
    val previous: String?,
    val results: List<ShortPokemon>
)