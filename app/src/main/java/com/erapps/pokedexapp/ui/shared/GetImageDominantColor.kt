package com.erapps.pokedexapp.ui.shared

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette

fun getImageDominantColor(domColor: MutableState<Color>, drawable: Drawable) {

    val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
    Palette.from(bitmap).generate { palette ->
        palette?.vibrantSwatch?.rgb?.let { colorValue ->
            domColor.value = Color(colorValue)
        }
    }
}