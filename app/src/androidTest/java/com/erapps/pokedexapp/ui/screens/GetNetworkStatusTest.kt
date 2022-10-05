package com.erapps.pokedexapp.ui.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.filters.MediumTest
import com.erapps.pokedexapp.ui.theme.PokedexAppTheme
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@MediumTest
class GetNetworkStatusTest {
    @get:Rule
    val composeRule = createComposeRule()

    private var status: Boolean = false

    @Before
    fun setUp() {
        composeRule.setContent {
            PokedexAppTheme {
                status = getNetworkStatus()
            }
        }
    }

    @Test
    fun netWorkStatusIsWork() {

        assertThat(status).isTrue()
    }
}