package com.erapps.pokedexapp.data.api.models.pokemon

data class HeldItem(
    val item: Item,
    val version_details: List<VersionDetail>
)