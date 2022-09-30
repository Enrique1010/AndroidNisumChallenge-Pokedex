package com.erapps.pokedexapp.utils

fun String.getIdFromUrl(): String {
    return Regex("[0-9]+").findAll(this)
        .map(MatchResult::value)
        .toList()[1]
}