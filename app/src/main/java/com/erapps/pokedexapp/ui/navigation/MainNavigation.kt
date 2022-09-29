package com.erapps.pokedexapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.erapps.pokedexapp.ui.screens.details.DetailsScreen
import com.erapps.pokedexapp.ui.screens.pokemonlist.PokemonListScreen

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavItem.PokemonList.baseRoute) {
        composable(NavItem.PokemonList) {
            PokemonListScreen() {
                navController.navigate(NavItem.Details.createDetailsRoute(it)) {
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
        composable(NavItem.Details) {
            val pokemonId = it.arguments?.getString("pokemonId").toString()

            DetailsScreen(pokemonId = pokemonId) {
                navController.popBackStack()
            }
        }
    }
}

private fun NavGraphBuilder.composable(
    navItem: NavItem,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(route = navItem.route, arguments = navItem.args) {
        content(it)
    }
}