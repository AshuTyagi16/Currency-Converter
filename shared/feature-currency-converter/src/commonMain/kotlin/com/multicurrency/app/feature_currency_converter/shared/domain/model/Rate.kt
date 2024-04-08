package com.multicurrency.app.feature_currency_converter.shared.domain.model

data class Rate(
    val currencySymbol: String,
    val rate: Double,
    val uniqueId: String = currencySymbol.plus(rate)
)