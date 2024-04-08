package com.multicurrency.app.feature_currency_converter.shared.ui

import com.multicurrency.app.core_base.shared.ui.UiEffect
import com.multicurrency.app.core_base.shared.ui.UiEvent
import com.multicurrency.app.core_base.shared.ui.UiState
import com.multicurrency.app.feature_currency_converter.shared.domain.model.Currency
import com.multicurrency.app.feature_currency_converter.shared.domain.model.Rate

class HomePageContract {
    sealed interface Event : UiEvent {
        data object OnFetchHomePageDataEvent : Event
        data object OnFetchAllCurrenciesEvent : Event
        data object OnFetchAllRatesEvent : Event
        data class OnCurrencySelectedEvent(val index: Int) : Event
        data class OnAmountUpdated(val amount: Double) : Event
    }

    data class State(
        val isLoadingCurrencies: Boolean,
        val isLoadingRates: Boolean,
        val amount: String,
        val baseCurrency: String,
        val currencies: List<Currency>,
        val rates: List<Rate>
    ) : UiState


    sealed interface Effect : UiEffect {
        data class ShowToast(val message: String) : Effect
    }
}