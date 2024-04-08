package com.multicurrency.app.core_network.shared.api.data.base

import com.multicurrency.app.core_network.shared.api.data.model.NetworkResult
import com.multicurrency.app.core_network.shared.impl.util.NetworkConstants.NetworkErrorCodes
import com.multicurrency.app.core_network.shared.impl.util.NetworkConstants.NetworkErrorMessages
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.JsonConvertException
import io.ktor.util.network.UnresolvedAddressException
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.serialization.SerializationException

abstract class BaseDataSource {

    protected suspend inline fun <reified T> getResult(call: () -> HttpResponse): NetworkResult<T> {
        val result: HttpResponse?
        return try {
            result = call()
            if (result.status == HttpStatusCode.OK) {
                val data: T = result.body()
                NetworkResult.success(data)
            } else {
                NetworkResult.error(
                    errorMessage = NetworkErrorMessages.SOME_ERROR_OCCURRED,
                    errorCode = result.status.value
                )
            }
        } catch (e: ServerResponseException) {
            val statusCode = e.response.status.value
            NetworkResult.error(
                errorMessage = NetworkErrorMessages.APP_UNDER_MAINTENANCE,
                errorCode = statusCode
            )
        } catch (e: IOException) {
            NetworkResult.error(
                errorMessage = NetworkErrorMessages.PLEASE_CHECK_YOUR_INTERNET_CONNECTION,
                errorCode = NetworkErrorCodes.INTERNET_NOT_WORKING
            )
        } catch (e: UnresolvedAddressException) {
            NetworkResult.error(
                errorMessage = NetworkErrorMessages.PLEASE_CHECK_YOUR_INTERNET_CONNECTION,
                errorCode = NetworkErrorCodes.INTERNET_NOT_WORKING
            )
        } catch (e: SocketTimeoutException) {
            NetworkResult.error(
                errorMessage = NetworkErrorMessages.PLEASE_CHECK_YOUR_INTERNET_CONNECTION,
                errorCode = NetworkErrorCodes.INTERNET_NOT_WORKING
            )
        } catch (e: SerializationException) {
            NetworkResult.error(
                errorMessage = NetworkErrorMessages.DATA_SERIALIZATION_ERROR + "" + e.message,
                errorCode = NetworkErrorCodes.DATA_SERIALIZATION_ERROR,
            )
        } catch (e: JsonConvertException) {
            NetworkResult.error(
                errorMessage = NetworkErrorMessages.DATA_SERIALIZATION_ERROR + "" + e.message,
                errorCode = NetworkErrorCodes.DATA_SERIALIZATION_ERROR
            )
        } catch (e: CancellationException) {
            NetworkResult.error(
                errorMessage = "",  //This is a special case in which we don't want to show any error
                errorCode = NetworkErrorCodes.NETWORK_CALL_CANCELLED
            )
        } catch (e: Exception) {
            NetworkResult.error(
                errorMessage = e.message ?: NetworkErrorMessages.SOME_ERROR_OCCURRED,
                errorCode = NetworkErrorCodes.UNKNOWN_ERROR_OCCURRED
            )
        }
    }

    protected suspend fun <T> retryIOs(
        times: Int = Int.MAX_VALUE,
        initialDelay: Long = 100,
        maxDelay: Long = 1000,
        factor: Double = 2.0,
        block: suspend () -> T,
        shouldRetry: (result: T) -> Boolean
    ): T {
        var currentDelay = initialDelay
        repeat(times - 1) {
            val result = block()
            if (shouldRetry(result).not()) {
                return result
            }
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
        }
        return block()
    }
    
}