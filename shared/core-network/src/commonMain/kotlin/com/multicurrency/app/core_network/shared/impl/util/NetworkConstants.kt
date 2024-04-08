package com.multicurrency.app.core_network.shared.impl.util

object NetworkConstants {

    object NetworkErrorCodes {
        const val INTERNET_NOT_WORKING = 712
        const val UNKNOWN_ERROR_OCCURRED = 713
        const val NETWORK_CALL_CANCELLED = 714
        const val DATA_SERIALIZATION_ERROR = 715
    }

    object NetworkErrorMessages {
        const val SOME_ERROR_OCCURRED = "Some error occurred"
        const val APP_UNDER_MAINTENANCE = "App under maintenance"
        const val PLEASE_CHECK_YOUR_INTERNET_CONNECTION = "Please check your internet connection"
        const val DATA_SERIALIZATION_ERROR = "Data serialization error"
    }

}