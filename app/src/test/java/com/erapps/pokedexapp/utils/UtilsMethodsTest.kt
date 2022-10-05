package com.erapps.pokedexapp.utils

import org.junit.Test

class UtilsMethodsTest {

    @Test
    fun `Get last number from Url`() {
        val url = "https://pokeapp/v2/2"
        val text = url.getIdFromUrl()

        assert(text == "2")
    }

    @Test
    fun `Capitalize first letter of text and replace (-) with spaces`() {
        val badTitle = "pikachu-great"
        val goodTitle = badTitle.makeGoodTitle()

        assert(goodTitle == "Pikachu great")
    }

    @Test
    fun `Abbreviate pokemon stat`() {
        val normalStat = "attack"
        val abbreviateStat = abbrStat(normalStat)

        assert(abbreviateStat == "Atk")
    }
}