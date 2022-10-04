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
        fun createRoute(url: String, colorRGB: Int): String {
            val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
            return "$baseRoute/$encodedUrl/$colorRGB"
        }
    }

    object MoveDetails :
        NavItem("moveDetails", listOf(NavArgs.MoveDetailsUrl, NavArgs.MoveColorRGB)) {
        fun createRoute(url: String, colorRGB: Int): String {
            val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
            return "$baseRoute/$encodedUrl/$colorRGB"
        }
    }

    object SpecieDetails :
        NavItem("specieDetails", listOf(NavArgs.SpecieUrl, NavArgs.SpecieColorRGB)) {
        fun createRoute(url: String, colorRGB: Int): String {
            val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
            return "$baseRoute/$encodedUrl/$colorRGB"
        }
    }

    object Encounters :
        NavItem("encounters", listOf(NavArgs.EncountersUrl, NavArgs.EncountersColorRGB)) {
        fun createRoute(url: String, colorRGB: Int): String {
            val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
            return "$baseRoute/$encodedUrl/$colorRGB"
        }
    }

    object LocationAreas :
        NavItem("locationAreas", listOf(NavArgs.LocationAreasUrl, NavArgs.LocationsAreaColorRGB)) {
        fun createRoute(url: String, colorRGB: Int): String {
            val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
            return "$baseRoute/$encodedUrl/$colorRGB"
        }
    }
}

enum class NavArgs(
    val key: String,
    val navType: NavType<*>
) {
    //details
    PokemonId("pokemonId", NavType.StringType),

    //abilities
    AbilityUrl("abilityUrl", NavType.StringType),
    AbilityColorRGB("abilityColorRGB", NavType.IntType),

    //moves
    MoveDetailsUrl("moveUrl", NavType.StringType),
    MoveColorRGB("moveColorRGB", NavType.IntType),

    //specie
    SpecieUrl("specieUrl", NavType.StringType),
    SpecieColorRGB("specieColorRGB", NavType.IntType),

    //locations
    EncountersUrl("encountersUrl", NavType.StringType),
    EncountersColorRGB("encountersColorRGB", NavType.IntType),
    LocationAreasUrl("locationAreaUrl", NavType.StringType),
    LocationsAreaColorRGB("locationAreaColorRGB", NavType.IntType)
}
