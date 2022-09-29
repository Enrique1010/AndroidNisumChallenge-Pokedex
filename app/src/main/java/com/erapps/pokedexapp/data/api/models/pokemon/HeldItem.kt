package com.erapps.pokedexapp.data.api.models

data class HeldItem(
    val item: Item,
    val version_details: List<VersionDetail>
)