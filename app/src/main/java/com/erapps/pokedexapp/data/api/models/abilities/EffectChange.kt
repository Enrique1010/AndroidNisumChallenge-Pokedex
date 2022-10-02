package com.erapps.pokedexapp.data.api.models.abilities

data class EffectChange(
    val effect_entries: List<EffectEntry>,
    val version_group: VersionGroup
)