package com.multicurrency.app.core_network.shared.api.data.model

data class NetworkResult<out T>(
    val status: Status,
    val data: T? = null,
    val errorMessage: String? = null,
    val errorCode: Int? = null
) {
    enum class Status {
        SUCCESS,
        ERROR,
        LOADING,
        IDLE
    }

    companion object {

        fun <T> idle(): NetworkResult<T> {
            return NetworkResult(status = Status.IDLE)
        }

        fun <T> loading(): NetworkResult<T> {
            return NetworkResult(status = Status.LOADING)
        }

        fun <T> success(data: T): NetworkResult<T> {
            return NetworkResult(
                status = Status.SUCCESS,
                data = data
            )
        }

        fun <T> error(errorMessage: String?, errorCode: Int? = null): NetworkResult<T> {
            return NetworkResult(
                status = Status.ERROR,
                errorMessage = errorMessage,
                errorCode = errorCode
            )
        }
    }
}