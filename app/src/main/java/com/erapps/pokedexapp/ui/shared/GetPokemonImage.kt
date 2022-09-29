package com.erapps.pokedexapp.ui.shared

import com.erapps.pokedexapp.BuildConfig

const val POKEMON_IMAGE_PATH = BuildConfig.PokeApi_Sprites_URL

fun getPokemonImage(pokemonId: Int): String {
    return "$POKEMON_IMAGE_PATH/$pokemonId.png"
}