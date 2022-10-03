package com.erapps.pokedexapp.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class NavItem(
    val baseRoute: String,
    private val navArgs: List<NavArgs> = emptyList()
) {
    val route = run {
        val argKeys = navArgs.map { "{${it.key}}" }
        listOf(baseRoute).plus(argKeys).joinToString("/")
    }

    val args = navArgs.map { navArgument(name = it.key) { type = it.navType } }

    //navigation objects
    object PokemonList : NavItem("pokemon_list")
    object Details : NavItem("details", listOf(NavArgs.PokemonId)) {
        fun createDetailsRoute(pokemonId: String) = "$baseRoute/$pokemonId"
    }

    //details navigation
    object AbilityDetails :
        NavItem("abilityDetails", listOf(NavArgs.AbilityUrl, NavArgs.AbilityColorRGB)) {
        fun createRoute(url: String, colorARG: Int): String {
            val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
            return "$baseRoute/$encodedUrl/$colorARG"
        }
    }

    object Move : NavItem("moves", listOf(NavArgs.MovesUrl)) {
        fun createRoute(url: String): String {
            val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
            return "$baseRoute/$url"
        }
    }

    object MoveDetails : NavItem("moveDetails", listOf(NavArgs.MoveDetailsUrl)) {
        fun createRoute(url: String): String {
            val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
            return "$baseRoute/$url"
        }
    }

    object SpecieDetails : NavItem("specieDetails", listOf(NavArgs.SpecieUrl)) {
        fun createRoute(url: String): String {
            val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
            return "$baseRoute/$url"
        }
    }

    object Encounters : NavItem("encounters", listOf(NavArgs.EncountersUrl)) {
        fun createRoute(url: String): String {
            val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
            return "$baseRoute/$url"
        }
    }

    object LocationAreas : NavItem("locationAreas", listOf(NavArgs.LocationAreasUrl)) {
        fun createRoute(url: String): String {
            val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
            return "$baseRoute/$url"
        }
    }
}

enum class NavArgs(
    val key: String,
    val navType: NavType<*>
) {
    PokemonId("pokemonId", NavType.StringType),
    AbilityUrl("abilityUrl", NavType.StringType),
    AbilityColorRGB("abilityColorRGB", NavType.IntType),
    MovesUrl("moveUrl", NavType.StringType),
    MoveDetailsUrl("moveUrl", NavType.StringType),
    SpecieUrl("specieUrl", NavType.StringType),
    EncountersUrl("encountersUrl", NavType.StringType),
    LocationAreasUrl("locationAreasUrl", NavType.StringType),
}
