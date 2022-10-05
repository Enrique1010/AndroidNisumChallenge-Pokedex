package com.erapps.pokedexapp.ui.screens.pokemonlist

import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.filters.LargeTest
import com.erapps.pokedexapp.data.api.models.ShortPokemon
import com.erapps.pokedexapp.ui.theme.PokedexAppTheme
import com.erapps.pokedexapp.utils.TestTags.LISTOFPOKEMONS
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@LargeTest
class ListOfPokemonsTest {
    @get:Rule
    val composeRule = createComposeRule()

    private var list = listOf(
        ShortPokemon(name = "pikachu 2", url = "https://pika/v2/2"),
        ShortPokemon(name = "pikachu 3", url = "https://pika/v2/3")
    )

    @Before
    fun setUp() {
        composeRule.setContent {
            PokedexAppTheme {
                ListOfPokemons(
                    list = list,
                    onCardClick = {},
                    scrollState = rememberLazyGridState()
                )
            }
        }
    }

    @Test
    fun listIfDisplayedIfHasPokemons() {

        composeRule.onNodeWithTag(LISTOFPOKEMONS).assertIsDisplayed()
    }
}