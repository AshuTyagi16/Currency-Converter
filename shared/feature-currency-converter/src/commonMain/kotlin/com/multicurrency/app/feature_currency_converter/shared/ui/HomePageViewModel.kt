package com.multicurrency.app.feature_currency_converter.shared.ui

import com.multicurrency.app.core_base.shared.ui.BaseViewModel
import com.multicurrency.app.core_network.shared.impl.util.collect
import com.multicurrency.app.feature_currency_converter.shared.domain.use_case.FetchAllCurrenciesUseCase
import com.multicurrency.app.feature_currency_converter.shared.domain.use_case.FetchAllRatesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomePageViewModel(
    private val fetchAllCurrenciesUseCase: FetchAllCurrenciesUseCase,
    private val fetchAllRatesUseCase: FetchAllRatesUseCase
) : BaseViewModel<HomePageContract.Event, HomePageContract.State, HomePageContract.Effect>() {

    private var fetchRatesJob: Job? = null

    private var updateStateJob: Job? = null

    init {
        init()
    }

    fun init() {
        scope.launch {
            subscribeEvents()
        }
    }

    override fun createInitialState(): HomePageContract.State {
        return HomePageContract.State(
            isLoadingCurrencies = false,
            isLoadingRates = false,
            amount = "0",
            baseCurrency = "USD",
            currencies = emptyList(),
            rates = emptyList()
        )
    }

    override fun handleEvent(event: HomePageContract.Event) {
        when (event) {
            is HomePageContract.Event.OnAmountUpdated -> handleAmountUpdatedEvent(amount = event.amount)
            is HomePageContract.Event.OnCurrencySelectedEvent -> handleCurrencySelectedEvent(
                index = event.index
            )

            HomePageContract.Event.OnFetchAllCurrenciesEvent -> fetchAllCurrencies()
            HomePageContract.Event.OnFetchAllRatesEvent -> fetchAllRatesForValue()
            HomePageContract.Event.OnFetchHomePageDataEvent -> {
                fetchAllCurrencies()
                fetchAllRatesForValue()
            }
        }
    }

    private fun handleAmountUpdatedEvent(amount: Double) {
        updateStateJob?.cancel()
        updateStateJob = scope.launch {
            delay(300) // act as debounce
            setState {
                copy(
                    amount = amount.toString()
                )
            }
            fetchAllRatesForValue()
        }
    }

    private fun handleCurrencySelectedEvent(index: Int) {
        updateStateJob?.cancel()
        updateStateJob = scope.launch {
            delay(300) // act as debounce
            val selectedCurrency = uiState.value.currencies[index].currencySymbol
            setState {
                copy(
                    baseCurrency = selectedCurrency
                )
            }
            fetchAllRatesForValue()
        }
    }

    private fun fetchAllCurrencies() {
        scope.launch {
            fetchAllCurrenciesUseCase.invoke().collect(
                onLoading = {
                    setState {
                        copy(
                            isLoadingCurrencies = true
                        )
                    }
                },
                onSuccess = {
                    setState {
                        copy(
                            isLoadingCurrencies = false,
                            currencies = it
                        )
                    }
                },
                onError = { errorMessage, _ ->
                    setState {
                        copy(
                            isLoadingCurrencies = false,
                            currencies = this.currencies
                        )
                    }
                    setEffect {
                        HomePageContract.Effect.ShowToast(message = errorMessage)
                    }
                }
            )
        }
    }

    private fun fetchAllRatesForValue() {
        fetchRatesJob?.cancel()
        fetchRatesJob = scope.launch {
            fetchAllRatesUseCase.fetchAllRatesForValues(
                amount = uiState.value.amount.toDoubleOrNull() ?: 0.0,
                baseCurrency = uiState.value.baseCurrency
            ).collect(
                onLoading = {
                    setState {
                        copy(
                            isLoadingRates = true,
                            rates = this.rates
                        )
                    }
                },
                onSuccess = {
                    setState {
                        copy(
                            isLoadingRates = false,
                            rates = it
                        )
                    }
                },
                onError = { errorMessage, _ ->
                    setState {
                        copy(
                            isLoadingRates = false,
                            rates = this.rates
                        )
                    }
                    setEffect {
                        HomePageContract.Effect.ShowToast(message = errorMessage)
                    }
                }
            )
        }
    }
}