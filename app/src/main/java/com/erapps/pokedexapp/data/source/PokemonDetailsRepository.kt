package com.erapps.pokedexapp.data.source

import com.erapps.pokedexapp.data.Result
import com.erapps.pokedexapp.data.api.models.Pokemon
import com.erapps.pokedexapp.data.api.models.abilities.AbilityDetails
import com.erapps.pokedexapp.data.api.models.encounters.PokemonEncounters
import com.erapps.pokedexapp.data.api.models.encounters.locationarea.LocationAreaDetails
import com.erapps.pokedexapp.data.api.models.moves.MoveDetails
import com.erapps.pokedexapp.data.api.models.species.SpeciesDetails
import com.erapps.pokedexapp.data.api.models.species.evolutionchain.EvolutionChainDetails
import com.erapps.pokedexapp.data.source.remote.PokemonDetailsRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface PokemonDetailsRepository {
    fun getPokemon(pokemonId: String): Flow<Result<Pokemon, *>>
    //details methods
    fun getAbilityDetails(url: String): Flow<Result<AbilityDetails, *>>
    fun getMoveDetails(url: String): Flow<Result<MoveDetails, *>>
    fun getPokemonSpecieDetails(url: String): Flow<Result<SpeciesDetails, *>>
    fun getEvolutionChain(url: String): Flow<Result<EvolutionChainDetails, *>>
    fun getEncounters(url: String): Flow<Result<PokemonEncounters, *>>
    fun getLocationAreas(url: String): Flow<Result<LocationAreaDetails, *>>
}

class PokemonDetailsRepositoryImp @Inject constructor(
    private val pokemonDetailsRemoteDataSource: PokemonDetailsRemoteDataSource
): PokemonDetailsRepository {

    override fun getPokemon(pokemonId: String): Flow<Result<Pokemon, *>> {
        return pokemonDetailsRemoteDataSource.getPokemon(pokemonId).flowOn(Dispatchers.Default)
    }

    override fun getAbilityDetails(url: String): Flow<Result<AbilityDetails, *>> {
        return pokemonDetailsRemoteDataSource.getAbilityDetails(url).flowOn(Dispatchers.Default)
    }

    override fun getMoveDetails(url: String): Flow<Result<MoveDetails, *>> {
        return pokemonDetailsRemoteDataSource.getMoveDetails(url).flowOn(Dispatchers.Default)
    }

    override fun getPokemonSpecieDetails(url: String): Flow<Result<SpeciesDetails, *>> {
       return pokemonDetailsRemoteDataSource.getPokemonSpecieDetails(url).flowOn(Dispatchers.Default)
    }

    override fun getEvolutionChain(url: String): Flow<Result<EvolutionChainDetails, *>> {
        return pokemonDetailsRemoteDataSource.getEvolutionChain(url).flowOn(Dispatchers.Default)
    }

    override fun getEncounters(url: String): Flow<Result<PokemonEncounters, *>> {
        return pokemonDetailsRemoteDataSource.getEncounters(url).flowOn(Dispatchers.Default)
    }

    override fun getLocationAreas(url: String): Flow<Result<LocationAreaDetails, *>> {
        return pokemonDetailsRemoteDataSource.getLocationAreas(url).flowOn(Dispatchers.Default)
    }

}