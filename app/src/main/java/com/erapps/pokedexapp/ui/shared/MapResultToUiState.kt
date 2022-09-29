package com.erapps.pokedexapp.ui.shared

import androidx.compose.runtime.MutableState
import com.erapps.pokedexapp.R
import java.net.SocketTimeoutException
import com.erapps.pokedexapp.data.Result

fun <T, E> mapResultToUiState(
    result: Result<T, E>,
    uiState: MutableState<UiState?>,
    successBlock: (T) -> Unit
) {
    when(result) {
        is Result.Error -> mapErrors(result, uiState)
        Result.Loading -> uiState.value = UiState.Loading
        is Result.Success -> successBlock(result.data!!)
    }
}

fun mapErrors(result: Result.Error<*>, uiState: MutableState<UiState?>) {
    when {
        result.isApiError -> {
            if (result.code in 500..599) {
                uiState.value = UiState.Error(errorStringResource = R.string.error_server)
                return
            }
            uiState.value = UiState.Error(errorMessage = result.exception?.message!!)
        }
        result.isNetworkError -> {
            if (result.exception is SocketTimeoutException) {
                uiState.value = UiState.Error(errorStringResource = R.string.timeout_error)
                return
            }
            //network error code
            uiState.value = UiState.Error(errorStringResource = R.string.unknown_error)
        }
        result.isUnknownError -> {
            uiState.value = UiState.Error(errorStringResource = R.string.unknown_error)
        }
    }
}