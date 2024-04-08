package com.multicurrency.app.feature_currency_converter.shared.domain.model

data class Currency(
    val currencySymbol: String,
    val currencyName: String
) {
    override fun toString(): String {
        return currencySymbol.plus(" ").plus(":").plus(" ").plus(currencyName)
    }
}