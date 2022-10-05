package com.erapps.pokedexapp.ui.screens.pokemonlist

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.filters.LargeTest
import com.erapps.pokedexapp.ui.theme.PokedexAppTheme
import com.erapps.pokedexapp.utils.TestTags.SEARCHBARICONBUTTON
import com.erapps.pokedexapp.utils.TestTags.SEARCHBARTEXTFIELD
import com.erapps.pokedexapp.utils.TestTags.TRAILINGICONBUTTON
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalAnimationApi::class)
@LargeTest
class SearchBarTest {
    @get:Rule
    val composeRule = createComposeRule()

    private val text = mutableStateOf("")
    private val focused = mutableStateOf(false)

    @Before
    fun setUp() {
        composeRule.setContent {
            PokedexAppTheme {
                SearchBar(
                    query = text,
                    onSearchClick = { },
                    onBack = {  },
                    focused = focused
                )
            }
        }
    }

    @Test
    fun focusIsClearedWhenIconBackButtonIsPressed() {
        // when
        composeRule.onNodeWithTag(SEARCHBARTEXTFIELD).performTextInput("pikachu")
        // asserting if focused when text is entered
        composeRule.onNodeWithTag(SEARCHBARTEXTFIELD)

        // clicking on close icon to remove focus
        composeRule.onNodeWithTag(SEARCHBARICONBUTTON).performClick()

        // asserting focus is lost
        composeRule.onNodeWithTag(SEARCHBARTEXTFIELD).assertIsNotFocused()
    }

    @Test
    fun fieldTextIsEqualToQuery() {
        val query = "pikachu"
        // when
        // asserting if text is correct
        composeRule.onNodeWithTag(SEARCHBARTEXTFIELD).performTextInput(query)
        assertThat(text.value).isEqualTo(query)
    }

    @Test
    fun focusIsKeptWhenImeActionIsPressed() {
        val query = "pikachu"
        // when
        composeRule.onNodeWithTag(SEARCHBARTEXTFIELD).performTextInput(query)
        // asserting focus is present
        composeRule.onNodeWithTag(SEARCHBARTEXTFIELD).assertIsFocused()

        // clicking on keyboard ime action
        composeRule.onNodeWithTag(SEARCHBARTEXTFIELD).performImeAction()

        // asserting focus is lost
        composeRule.onNodeWithTag(SEARCHBARTEXTFIELD).assertIsFocused()
    }

    @Test
    fun trailingIconButtonIsDisplayedWhenSearchBarIsPressed() {
        val query = "pikachu"
        // when
        composeRule.onNodeWithTag(SEARCHBARTEXTFIELD).performTextInput(query)
        // asserting focus is present
        composeRule.onNodeWithTag(SEARCHBARTEXTFIELD).assertIsFocused()

        composeRule.onNodeWithTag(TRAILINGICONBUTTON).assertIsDisplayed()
    }

    @Test
    fun trailingIconButtonIsNotHiddenWhenTextIsEmpty() {
        val query = ""
        // when
        composeRule.onNodeWithTag(SEARCHBARTEXTFIELD).performTextInput(query)
        // asserting focus is present
        composeRule.onNodeWithTag(SEARCHBARTEXTFIELD).assertIsFocused()

        composeRule.onNodeWithTag(TRAILINGICONBUTTON).assertExists()
    }
}