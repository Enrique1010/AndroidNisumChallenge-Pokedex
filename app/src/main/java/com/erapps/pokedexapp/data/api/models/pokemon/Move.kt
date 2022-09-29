package com.erapps.pokedexapp.data.api.models.pokemon

data class Move(
    val move: MoveX,
    val version_group_details: List<VersionGroupDetail>
)