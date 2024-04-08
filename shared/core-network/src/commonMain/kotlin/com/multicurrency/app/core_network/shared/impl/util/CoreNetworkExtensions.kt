package com.multicurrency.app.core_network.shared.impl.util

import com.multicurrency.app.core_network.shared.api.data.model.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.json.JsonElement

suspend inline fun <DTO, Domain> NetworkResult<DTO>.mapFromDTO(
    crossinline dataMapper: suspend (t: DTO?) -> Domain
): NetworkResult<Domain> {
    return when (status) {
        NetworkResult.Status.IDLE -> {
            NetworkResult.idle()
        }

        NetworkResult.Status.LOADING -> {
            NetworkResult.loading()
        }

        NetworkResult.Status.SUCCESS -> {
            NetworkResult.success(data = dataMapper.invoke(data))
        }

        NetworkResult.Status.ERROR -> {
            NetworkResult.error(errorMessage = errorMessage.orEmpty(), errorCode = errorCode)
        }
    }
}

suspend inline fun <DTO> NetworkResult<JsonElement>.mapFromJsonElement(
    crossinline dataMapper: suspend (jsonElement: JsonElement?) -> DTO
): NetworkResult<DTO> {
    return when (status) {
        NetworkResult.Status.IDLE -> {
            NetworkResult.idle()
        }

        NetworkResult.Status.LOADING -> {
            NetworkResult.loading()
        }

        NetworkResult.Status.SUCCESS -> {
            NetworkResult.success(data = dataMapper.invoke(data))
        }

        NetworkResult.Status.ERROR -> {
            NetworkResult.error(errorMessage = errorMessage.orEmpty(), errorCode = errorCode)
        }
    }
}

suspend fun <T> Flow<NetworkResult<T>>.collect(
    onLoading: suspend () -> Unit,
    onSuccess: suspend (data: T) -> Unit,
    onError: suspend (errorMessage: String, errorCode: Int?) -> Unit
) {
    this.collectLatest {
        when (it.status) {
            NetworkResult.Status.IDLE -> {
                // Do nothing
            }

            NetworkResult.Status.LOADING -> {
                onLoading.invoke()
            }

            NetworkResult.Status.SUCCESS -> {
                onSuccess.invoke(it.data!!)
            }

            NetworkResult.Status.ERROR -> {
                onError.invoke(it.errorMessage.orEmpty(), it.errorCode)
            }
        }
    }
}

fun <T> NetworkResult<T>.isLoading() = status == NetworkResult.Status.LOADING
fun <T> NetworkResult<T>.isSuccess() = status == NetworkResult.Status.SUCCESS
fun <T> NetworkResult<T>.isError() = status == NetworkResult.Status.ERROR