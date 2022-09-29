package com.erapps.pokedexapp.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.erapps.pokedexapp.ui.navigation.MainNavigation
import com.erapps.pokedexapp.ui.theme.PokedexAppTheme

@Composable
fun MainScreen() {
    PokedexAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            MainNavigation()
        }
    }
}