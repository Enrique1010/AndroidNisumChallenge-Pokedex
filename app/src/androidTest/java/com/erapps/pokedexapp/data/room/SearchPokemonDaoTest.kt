package com.erapps.pokedexapp.data.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.erapps.pokedexapp.data.api.models.ShortPokemon
import com.erapps.pokedexapp.data.room.entities.PokemonListEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@SmallTest
class SearchPokemonDaoTest {

    private lateinit var dataBase: PokeDexAppDataBase
    private lateinit var dao: SearchPokemonDao

    @Before
    fun setup() {
        dataBase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PokeDexAppDataBase::class.java
        ).allowMainThreadQueries().build()

        dao = dataBase.searchPokemonDao()
    }

    @After
    fun teardown() {
        dataBase.close()
    }

    @Test
    fun testInsertAndGetPokemonSearchList() = runTest {
        val pokemonListEntity =
            PokemonListEntity(pokemonList = listOf(ShortPokemon("pikachu", "https://pika/v2/26")))
        dao.insertPokemons(pokemonListEntity)

        val pokemons = dao.getCachedPokemons()

        assertThat(pokemons?.pokemonList?.get(0)?.name)
            .contains(pokemonListEntity.pokemonList[0].name)
    }

    @Test
    fun testClearPokemonEntity() = runTest {
        val pokemonListEntity =
            PokemonListEntity(pokemonList = listOf(ShortPokemon("pikachu", "https://pika/v2/26")))
        dao.insertPokemons(pokemonListEntity)

        dao.clearPokemons()

        val pokemons = dao.getCachedPokemons()

        assertThat(pokemons?.pokemonList).isNull()
    }

    @Test
    fun testLastDateUpdatedInsertIsCorrect() = runTest {
        val pokemonListEntity =
            PokemonListEntity(pokemonList = listOf(ShortPokemon("pikachu", "https://pika/v2/26")))
        dao.insertPokemons(pokemonListEntity)

        val currentMillis = System.currentTimeMillis()
        val insertedDateMillis = dao.getCachedPokemons()?.lastDateUpdated

        assertThat(currentMillis).isNotEqualTo(insertedDateMillis)
    }

    @Test
    fun testIdIsAlways0() = runTest {
        for (i in 1..10) {
            val pokemonListEntity =
                PokemonListEntity(pokemonList = listOf(ShortPokemon("pikachu $i", "https://pika/v2/2$i")))
            dao.insertPokemons(pokemonListEntity)
        }

        val pokemons = dao.getCachedPokemons()?.Id
        assertThat(pokemons).isEqualTo(0)
    }
}