package com.multicurrency.app.feature_currency_converter.shared.util

import com.multicurrency.app.feature_currency_converter.shared.data.dto.CurrencyDto
import com.multicurrency.app.feature_currency_converter.shared.data.dto.RateDto
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

fun JsonElement?.toRateDtoList(): List<RateDto> {
    return this?.jsonObject?.get("rates")?.jsonObject?.entries?.map {
        RateDto(
            currencySymbol = it.key,
            rate = it.value.jsonPrimitive.doubleOrNull ?: 0.0
        )
    }.orEmpty()
}

fun JsonElement?.toCurrencyDtoList(): List<CurrencyDto> {
    return this?.jsonObject?.entries?.map {
        CurrencyDto(
            currencySymbol = it.key,
            currencyName = it.value.jsonPrimitive.content
        )
    }.orEmpty()
}