package com.erapps.pokedexapp.utils

import androidx.test.filters.SmallTest
import com.erapps.pokedexapp.ui.theme.AccuracyColor
import com.erapps.pokedexapp.ui.theme.HPColor
import com.erapps.pokedexapp.ui.theme.SpAtkColor
import com.erapps.pokedexapp.ui.theme.TypeWater
import org.junit.Test

@SmallTest
class GetColorMethodsTest {

    @Test
    fun getCorrectStatColor() {
        val hpColor = getPokemonStatToColor("hp")

        assert(hpColor == HPColor)
    }

    @Test
    fun getCorrectMoveColor() {
        val accuracyColor = getMoveStatColor("accuracy")

        assert(accuracyColor == AccuracyColor)
    }

    @Test
    fun getCorrectSpecialConditionColor() {
        val legendaryColor = getSpecialConditionColor("legendary")

        assert(legendaryColor == SpAtkColor)
    }
    @Test
    fun getCorrectTypeColor() {
        val typeWaterColor = getPokemonTypeToColor("water")

        assert(typeWaterColor == TypeWater)
    }
}