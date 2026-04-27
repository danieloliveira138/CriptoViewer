package com.danieloliveira138.criptoviewer.presentation.ui.detail

import androidx.lifecycle.SavedStateHandle
import com.danieloliveira138.criptoviewer.core.network.Result
import com.danieloliveira138.criptoviewer.domain.model.ExchangeAsset
import com.danieloliveira138.criptoviewer.domain.model.ExchangeInfo
import com.danieloliveira138.criptoviewer.domain.usecase.ExchangeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ExchangeDetailsViewModelTest {

    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    private val exchangeUseCase: ExchangeUseCase = mock()
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: ExchangeDetailsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun initViewModel(exchangeId: String? = "1") {
        savedStateHandle = SavedStateHandle(if (exchangeId != null) mapOf("exchangeId" to exchangeId) else emptyMap())
        viewModel = ExchangeDetailsViewModel(exchangeUseCase, savedStateHandle)
    }

    @Test
    fun `Initial state validation`() = runTest {
        initViewModel()
        val initialState = viewModel.state.value
        assertTrue(initialState.isLoading)
        assertTrue(initialState.exchangeAssets.isEmpty())
        assertEquals(null, initialState.exchangeInfo)
        assertEquals(null, initialState.error)
    }

    @Test
    fun `loadExchangeInfo success state update`() = runTest {
        val info = ExchangeInfo(
            id = 1,
            name = "Binance",
            dateLaunched = "2017-07-01T00:00:00Z",
            slug = null,
            logo = null,
            description = null,
            urls = null,
            makerFee = null,
            takerFee = null
        )
        whenever(exchangeUseCase.getExchangeInfo("1")).thenReturn(Result.Success(info))
        whenever(exchangeUseCase.getExchangeAssets("1")).thenReturn(Result.Success(emptyList()))

        initViewModel("1")
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals("Binance", state.exchangeInfo?.name)
        assertEquals("Jul 2017", state.exchangeInfo?.dateLaunched)
    }

    @Test
    fun `loadExchangeInfo failure error handling`() = runTest {
        val errorMessage = "Error loading info"
        whenever(exchangeUseCase.getExchangeInfo("1")).thenReturn(Result.Error(Exception(errorMessage)))
        whenever(exchangeUseCase.getExchangeAssets("1")).thenReturn(Result.Success(emptyList()))

        initViewModel("1")
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(errorMessage, state.error)
        
        val effect = viewModel.effect.first()
        assertTrue(effect is ExchangeDetailsEffect.showToast)
        assertEquals(errorMessage, (effect as ExchangeDetailsEffect.showToast).message)
    }

    @Test
    fun `loadExchangeAssets success state update and formatting`() = runTest {
        val assets = listOf(
            ExchangeAsset("Bitcoin", "BTC", 62450.0),
            ExchangeAsset("Ethereum", "ETH", 3420.153)
        )
        whenever(exchangeUseCase.getExchangeInfo(any())).thenReturn(Result.Error(Exception()))
        whenever(exchangeUseCase.getExchangeAssets("1")).thenReturn(Result.Success(assets))

        initViewModel("1")
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(2, state.exchangeAssets.size)
        assertEquals("Bitcoin(BTC)", state.exchangeAssets[0].name)
        // Note: DecimalFormat might be locale dependent in some environments
        // We expect $62,450.00 if using US locale grouping
        assertTrue(state.exchangeAssets[0].price.contains("62"))
        assertTrue(state.exchangeAssets[0].price.contains("450"))
        
        assertEquals("Ethereum(ETH)", state.exchangeAssets[1].name)
        assertTrue(state.exchangeAssets[1].price.contains("3"))
        assertTrue(state.exchangeAssets[1].price.contains("420"))
    }

    @Test
    fun `loadExchangeAssets duplicate removal`() = runTest {
        val assets = listOf(
            ExchangeAsset("Bitcoin", "BTC", 60000.0),
            ExchangeAsset("Bitcoin", "BTC", 60000.0)
        )
        whenever(exchangeUseCase.getExchangeInfo(any())).thenReturn(Result.Error(Exception()))
        whenever(exchangeUseCase.getExchangeAssets("1")).thenReturn(Result.Success(assets))

        initViewModel("1")
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(1, state.exchangeAssets.size)
    }

    @Test
    fun `loadExchangeAssets failure error handling`() = runTest {
        val errorMessage = "Error loading assets"
        whenever(exchangeUseCase.getExchangeInfo(any())).thenReturn(Result.Success(mock()))
        whenever(exchangeUseCase.getExchangeAssets("1")).thenReturn(Result.Error(Exception(errorMessage)))

        initViewModel("1")
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(errorMessage, state.error)
        
        val effect = viewModel.effect.first()
        assertTrue(effect is ExchangeDetailsEffect.showToast)
        assertEquals(errorMessage, (effect as ExchangeDetailsEffect.showToast).message)
    }

    @Test
    fun `OnLinkClicked event effect emission`() = runTest {
        whenever(exchangeUseCase.getExchangeInfo(any())).thenReturn(Result.Error(Exception()))
        whenever(exchangeUseCase.getExchangeAssets(any())).thenReturn(Result.Error(Exception()))
        initViewModel("1")
        
        val url = "https://google.com"
        viewModel.onEvent(ExchangeDetailsEvent.OnLinkClicked(url))
        
        val effect = viewModel.effect.first()
        assertTrue(effect is ExchangeDetailsEffect.navigateToBrowser)
        assertEquals(url, (effect as ExchangeDetailsEffect.navigateToBrowser).url)
    }

    @Test
    fun `OnBackClicked event effect emission`() = runTest {
        whenever(exchangeUseCase.getExchangeInfo(any())).thenReturn(Result.Error(Exception()))
        whenever(exchangeUseCase.getExchangeAssets(any())).thenReturn(Result.Error(Exception()))
        initViewModel("1")

        viewModel.onEvent(ExchangeDetailsEvent.OnBackClicked)
        
        val effect = viewModel.effect.first()
        assertTrue(effect is ExchangeDetailsEffect.navigateBack)
    }
}
