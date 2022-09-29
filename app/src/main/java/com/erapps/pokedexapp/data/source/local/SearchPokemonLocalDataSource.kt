package com.erapps.pokedexapp.data.source.local

import javax.inject.Inject

interface SearchPokemonLocalDataSource {
    fun getPokemons()
    fun clearPokemons()
}

class SearchPokemonLocalDataSourceImp @Inject constructor(

): SearchPokemonLocalDataSource {

    override fun getPokemons() {
        TODO("Not yet implemented")
    }

    override fun clearPokemons() {
        TODO("Not yet implemented")
    }

}