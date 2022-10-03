package com.erapps.pokedexapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.erapps.pokedexapp.ui.screens.details.DetailsScreen
import com.erapps.pokedexapp.ui.screens.details.abilities.AbilityDetailsScreen
import com.erapps.pokedexapp.ui.screens.pokemonlist.PokemonListScreen

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavItem.PokemonList.baseRoute) {
        composable(NavItem.PokemonList) {
            PokemonListScreen {
                safeNavigate(navController, NavItem.Details.createDetailsRoute(it))
            }
        }
        composable(NavItem.Details) {
            DetailsScreen(
                onAbilityClick = { url, colorRGB ->
                    safeNavigate(navController, NavItem.AbilityDetails.createRoute(url, colorRGB))
                }
            ) {
                navController.popBackStack()
            }
        }
        //details calls
        composable(NavItem.AbilityDetails) {
            AbilityDetailsScreen() {
                navController.popBackStack()
            }
        }
    }
}

private fun safeNavigate(navController: NavHostController ,route: String) {
    navController.navigate(route) {
        launchSingleTop = true
        restoreState = true
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