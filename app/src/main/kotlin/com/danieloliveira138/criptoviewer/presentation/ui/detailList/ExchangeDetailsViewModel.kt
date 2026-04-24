package com.danieloliveira138.criptoviewer.presentation.ui.detailList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danieloliveira138.criptoviewer.core.exceptions.UnknownException
import com.danieloliveira138.criptoviewer.domain.model.CoinItem
import com.danieloliveira138.criptoviewer.domain.model.ExchangeAsset
import com.danieloliveira138.criptoviewer.domain.usecase.ExchangeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.danieloliveira138.criptoviewer.core.Result
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ExchangeDetailsViewModel @Inject constructor(
    private val exchangeUseCase: ExchangeUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val exchangeId: String? = savedStateHandle["exchangeId"]
    private val _state = MutableStateFlow(DetailListState())
    val state: StateFlow<DetailListState> = _state.asStateFlow()
    private val _effect = Channel<ExchangeDetailEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        loadExchangeInfo(exchangeId = exchangeId)
        loadExchangeAssets(exchangeId = exchangeId)
    }

    fun onEvent(event: DetailListEvent) {
        when (event) {
            is DetailListEvent.OnLinkClicked -> navigateToBrowser(event.link)
           is  DetailListEvent.OnBackClicked -> navigateBack()
        }
    }

    private fun navigateBack() {
        viewModelScope.launch { _effect.send(ExchangeDetailEffect.navigateBack )}
    }

    private fun navigateToBrowser(url: String) {
        viewModelScope.launch {
            _effect.send(ExchangeDetailEffect.navigateToBrowser(url))
        }
    }

    private fun loadExchangeInfo(exchangeId: String?) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when(val response = exchangeUseCase.getExchangeInfo(exchangeId)) {
                is Result.Success -> {
                    val result = response.data
                    val data = result.copy(dateLaunched = result.dateLaunched?.formatDate())
                    _state.update { it.copy(isLoading = false, exchangeInfo = data) }
                }
                is Result.Error -> {
                    _state.update { it.copy(isLoading = false, error = response.exception.message) }
                    _effect.send(
                        ExchangeDetailEffect
                            .showToast(response.exception.message ?: UnknownException().message )
                    )
                }
            }
        }
    }

    private fun loadExchangeAssets(exchangeId: String?) {
        viewModelScope.launch {
            val response = exchangeUseCase.getExchangeAssets(exchangeId)
            when(response) {
                is Result.Success -> {
                    val coinItems = response.data.toCoinItems()
                    _state.update { it.copy(exchangeAssets = coinItems) }
                }
                is Result.Error -> {
                    val exceptionMsg = response.exception.message
                    _state.update { it.copy(error = exceptionMsg) }
                    _effect.send(
                        ExchangeDetailEffect
                            .showToast(exceptionMsg ?: UnknownException().message )
                    )
                }
            }
        }
    }

    private fun List<ExchangeAsset>.toCoinItems(): List<CoinItem> {
        return map { asset ->
            val decimalFormat = DecimalFormat("#,##0.00")
            val price = decimalFormat.format(asset.currencyPrice)
            CoinItem(
                "${asset.currencyName}(${asset.currencySymbol})",
                "\$$price"
            )
        }.distinct()
    }
    private fun String.formatDate(): String? {
        val inputFormat = DateTimeFormatter.ISO_DATE_TIME
        val data = ZonedDateTime.parse(this, inputFormat)
        val outputFormatter = DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH)
        return data.format(outputFormatter)
    }
}
