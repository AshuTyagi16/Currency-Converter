package com.multicurrency.app.feature_currency_converter.shared.domain.use_case

import com.multicurrency.app.core_network.shared.api.data.model.NetworkResult
import com.multicurrency.app.feature_currency_converter.shared.domain.model.Currency
import kotlinx.coroutines.flow.Flow

@Suppress("FUN_INTERFACE_WITH_SUSPEND_FUNCTION") // TODO: Remove once KTIJ-7642 is fixed
fun interface FetchAllCurrenciesUseCase : suspend () -> Flow<NetworkResult<List<Currency>>>