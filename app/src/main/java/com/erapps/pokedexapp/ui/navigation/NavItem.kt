package com.erapps.pokedexapp.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class NavItem(
    val baseRoute: String,
    val navArgs: List<NavArgs> = emptyList()
) {
    val route = run {
        val argKeys = navArgs.map { "{${it.key}}" }
        listOf(baseRoute).plus(argKeys).joinToString("/")
    }

    val args = navArgs.map { navArgument(name = it.key) { type = it.navType } }

    //navigation objects
    object PokemonList: NavItem("pokemon_list")
    object Details: NavItem("details", listOf(NavArgs.PokemonId)) {
        fun createDetailsRoute(pokemonId: String) = "$baseRoute/$pokemonId"
    }
    //details navigation

}

enum class NavArgs(
    val key: String,
    val navType: NavType<*>
) {
    PokemonId("pokemonId", NavType.StringType)
}
