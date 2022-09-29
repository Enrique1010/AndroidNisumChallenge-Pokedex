package com.erapps.pokedexapp.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.erapps.pokedexapp.data.api.models.ShortPokemon
import com.erapps.pokedexapp.data.room.entities.PokemonListEntity

@Dao
interface SearchPokemonDao {

    @Query("select cachedPokemons from pokemon_list where id = 0")
    suspend fun getCachedPokemons(): List<ShortPokemon>

    @Query("select date from pokemon_list where id = 0")
    suspend fun getTimeMillis(): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemons(pokemonListEntity: PokemonListEntity)

    @Query("delete from pokemon_list where id = 0")
    suspend fun clearPokemons()
}