package com.multicurrency.app.feature_currency_converter.shared.util

import com.multicurrency.app.feature_currency_converter.shared.data.dto.CurrencyDto
import com.multicurrency.app.feature_currency_converter.shared.data.dto.RateDto
import com.multicurrency.app.feature_currency_converter.shared.domain.model.Currency
import com.multicurrency.app.feature_currency_converter.shared.domain.model.Rate
import com.multicurrency.app.featurecurrencyconverter.shared.CurrencyEntity
import com.multicurrency.app.featurecurrencyconverter.shared.RateEntity


fun RateEntity.toRate(): Rate {
    return Rate(
        currencySymbol = currencySymbol,
        rate = rate
    )
}

fun RateDto.toRate(): Rate {
    return Rate(
        currencySymbol = currencySymbol,
        rate = rate
    )
}

fun List<RateDto>.fromDtoToRatesList(): List<Rate> {
    return this.map { it.toRate() }
}

fun List<RateEntity>.fromEntityToRatesList(): List<Rate> {
    return this.map { it.toRate() }
}

fun CurrencyEntity.toCurrency(): Currency {
    return Currency(
        currencySymbol = currencySymbol,
        currencyName = currencyName
    )
}

fun CurrencyDto.toCurrency(): Currency {
    return Currency(
        currencySymbol = currencySymbol,
        currencyName = currencyName
    )
}

fun List<CurrencyDto>.fromDtoToCurrencyList(): List<Currency> {
    return this.map { it.toCurrency() }
}

fun List<CurrencyEntity>.fromEntityToCurrencyList(): List<Currency> {
    return this.map { it.toCurrency() }
}