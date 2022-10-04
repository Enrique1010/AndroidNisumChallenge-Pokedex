package com.erapps.pokedexapp.data.source.remote

import com.erapps.pokedexapp.data.Result
import com.erapps.pokedexapp.data.api.PokeApiService
import com.erapps.pokedexapp.data.api.models.Pokemon
import com.erapps.pokedexapp.data.api.models.abilities.AbilityDetails
import com.erapps.pokedexapp.data.api.models.encounters.PokemonEncounters
import com.erapps.pokedexapp.data.api.models.encounters.locationarea.LocationAreaDetails
import com.erapps.pokedexapp.data.api.models.moves.MoveDetails
import com.erapps.pokedexapp.data.api.models.species.SpeciesDetails
import com.erapps.pokedexapp.data.api.models.species.evolutionchain.EvolutionChainDetails
import com.erapps.pokedexapp.data.source.mapResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface PokemonDetailsRemoteDataSource {
    fun getPokemon(pokemonId: String): Flow<Result<Pokemon, *>>
    //details methods
    fun getAbilityDetails(url: String): Flow<Result<AbilityDetails, *>>
    fun getMoveDetails(url: String): Flow<Result<MoveDetails, *>>
    fun getPokemonSpecieDetails(url: String): Flow<Result<SpeciesDetails, *>>
    fun getEvolutionChain(url: String): Flow<Result<EvolutionChainDetails, *>>
    fun getEncounters(url: String): Flow<Result<PokemonEncounters, *>>
    fun getLocationAreas(url: String): Flow<Result<LocationAreaDetails, *>>
}

class PokemonDetailsRemoteDataSourceImp @Inject constructor(
    private val pokeApiService: PokeApiService
): PokemonDetailsRemoteDataSource {

    override fun getPokemon(pokemonId: String): Flow<Result<Pokemon, *>> = mapResponse(Dispatchers.IO) {
        pokeApiService.getPokemon(pokemonId)
    }

    override fun getAbilityDetails(url: String): Flow<Result<AbilityDetails, *>> = mapResponse(Dispatchers.IO) {
        pokeApiService.getAbilityDetails(url)
    }

    override fun getMoveDetails(url: String): Flow<Result<MoveDetails, *>> = mapResponse(Dispatchers.IO) {
        pokeApiService.getMoveDetails(url)
    }

    override fun getPokemonSpecieDetails(url: String): Flow<Result<SpeciesDetails, *>> = mapResponse(Dispatchers.IO) {
        pokeApiService.getPokemonSpecieDetails(url)
    }

    override fun getEvolutionChain(url: String): Flow<Result<EvolutionChainDetails, *>> = mapResponse(Dispatchers.IO) {
        pokeApiService.getEvolutionChain(url)
    }

    override fun getEncounters(url: String): Flow<Result<PokemonEncounters, *>> = mapResponse(Dispatchers.IO) {
        pokeApiService.getEncounters(url)
    }

    override fun getLocationAreas(url: String): Flow<Result<LocationAreaDetails, *>> = mapResponse(Dispatchers.IO) {
        pokeApiService.getLocationAreas(url)
    }

}