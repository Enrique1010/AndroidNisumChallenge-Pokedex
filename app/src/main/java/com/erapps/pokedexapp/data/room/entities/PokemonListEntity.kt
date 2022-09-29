package com.erapps.pokedexapp.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.erapps.pokedexapp.data.api.models.ShortPokemon

@Entity(tableName = "pokemon_list")
data class PokemonListEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val Id: Int = 0,
    @ColumnInfo(name = "cachedPokemons")
    val pokemonList: List<ShortPokemon>,
    @ColumnInfo(name = "date")
    val lastDateUpdated: Long = System.currentTimeMillis()
)