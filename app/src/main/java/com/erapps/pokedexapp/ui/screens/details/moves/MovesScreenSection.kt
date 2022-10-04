package com.erapps.pokedexapp.ui.screens.details.moves

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.SwipeDown
import androidx.compose.material.icons.filled.SwipeUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.erapps.pokedexapp.R
import com.erapps.pokedexapp.data.api.models.pokemon.Move
import com.erapps.pokedexapp.ui.screens.getNetworkStatus
import com.erapps.pokedexapp.utils.makeGoodTitle

@Composable
fun MovesScreenSection(
    previousBackGroundColor: MutableState<Color>,
    moves: List<Move>,
    onMoveClick: (String, Int) -> Unit,
    isExpanded: Boolean
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colors.surface,
                        MaterialTheme.colors.surface,
                        previousBackGroundColor.value,
                        MaterialTheme.colors.surface
                    )
                )
            )
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MovesHeaderSection(isExpanded = isExpanded)
            LazyColumn {
                items(moves) { move ->
                    MoveListItem(
                        move = move,
                        color = previousBackGroundColor.value,
                        onMoveClick = onMoveClick
                    )
                }
            }
        }
    }
}

@Composable
fun MovesHeaderSection(
    modifier: Modifier = Modifier,
    isExpanded: Boolean
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = modifier.padding(dimensionResource(id = R.dimen.dimen_16dp)),
            text = stringResource(id = R.string.moves_label),
            fontSize = dimensionResource(id = R.dimen.common_screen_font_size).value.sp,
            fontWeight = FontWeight.Bold
        )
        Icon(
            modifier = modifier.padding(dimensionResource(id = R.dimen.dimen_16dp)),
            imageVector = if (!isExpanded) Icons.Default.SwipeUp else Icons.Default.SwipeDown,
            contentDescription = null
        )
    }
    Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.dimen_16dp)))
}

@Composable
fun MoveListItem(
    modifier: Modifier = Modifier,
    move: Move,
    onMoveClick: (String, Int) -> Unit,
    color: Color
) {
    val context = LocalContext.current
    val status = getNetworkStatus()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.dimen_4dp))
            .shadow(
                dimensionResource(id = R.dimen.basic_border),
                RoundedCornerShape(dimensionResource(id = R.dimen.dimen_4dp))
            )
            .background(MaterialTheme.colors.surface)
            .clickable {
                if (!status) {
                    Toast
                        .makeText(
                            context,
                            R.string.cant_see_details_without_internet_text,
                            Toast.LENGTH_SHORT
                        )
                        .show()
                    return@clickable
                }
                onMoveClick(move.move.url, color.toArgb())
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.dimen_16dp)),
            text = move.move.name.makeGoodTitle(),
            fontSize = dimensionResource(id = R.dimen.dimen_16dp).value.sp,
            fontWeight = FontWeight.Bold
        )
        Icon(
            modifier = modifier.clickable { onMoveClick(move.move.url, color.toArgb()) },
            imageVector = Icons.Default.NavigateNext,
            contentDescription = null
        )
    }
}