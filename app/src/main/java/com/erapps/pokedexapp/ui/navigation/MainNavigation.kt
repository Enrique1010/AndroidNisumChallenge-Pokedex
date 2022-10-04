package com.erapps.pokedexapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.erapps.pokedexapp.ui.screens.details.DetailsScreen
import com.erapps.pokedexapp.ui.screens.details.abilities.AbilityDetailsScreen
import com.erapps.pokedexapp.ui.screens.details.encounters.EncountersScreen
import com.erapps.pokedexapp.ui.screens.details.encounters.locationarea.LocationAreaScreen
import com.erapps.pokedexapp.ui.screens.details.moves.movedetails.MoveDetailsScreen
import com.erapps.pokedexapp.ui.screens.details.species.SpecieDetailsScreen
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
        detailsGraph(navController)
    }
}

private fun NavGraphBuilder.detailsGraph(navController: NavHostController) {

    navigation(startDestination = NavItem.Details.route, route = NavItem.Details.baseRoute) {
        composable(NavItem.Details) {
            DetailsScreen(
                onAbilityClick = { url, colorRGB ->
                    safeNavigate(navController, NavItem.AbilityDetails.createRoute(url, colorRGB))
                },
                onMoveClick = { url, colorRGB ->
                    safeNavigate(navController, NavItem.MoveDetails.createRoute(url, colorRGB))
                },
                onEncounterSectionClick = { url, colorRGB ->
                    safeNavigate(navController, NavItem.Encounters.createRoute(url, colorRGB))
                },
                onSpecieSectionClick = { url, colorRGB ->
                    safeNavigate(navController ,NavItem.SpecieDetails.createRoute(url, colorRGB))
                },
                onBackPressed = { navController.popBackStack() }
            )
        }
        //details calls
        composable(NavItem.AbilityDetails) {
            AbilityDetailsScreen {
                navController.popBackStack()
            }
        }
        composable(NavItem.MoveDetails) {
            MoveDetailsScreen {
                navController.popBackStack()
            }
        }
        encountersGraph(navController)
        composable(NavItem.SpecieDetails) {
            SpecieDetailsScreen {
                navController.popBackStack()
            }
        }
    }
}

private fun NavGraphBuilder.encountersGraph(navController: NavHostController) {

    navigation(startDestination = NavItem.Encounters.route, route = NavItem.Encounters.baseRoute) {
        composable(NavItem.Encounters.route) {
            EncountersScreen(
                onLocationClick = { url, colorRGB ->
                    safeNavigate(navController, NavItem.LocationAreas.createRoute(url, colorRGB))
                },
                onBackPressed = { navController.popBackStack() }
            )
        }
        composable(NavItem.LocationAreas) {
            LocationAreaScreen {
                navController.popBackStack()
            }
        }
    }
}

private fun safeNavigate(navController: NavHostController, route: String) {
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