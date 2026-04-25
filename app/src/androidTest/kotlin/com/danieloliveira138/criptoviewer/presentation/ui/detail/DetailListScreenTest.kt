package com.danieloliveira138.criptoviewer.presentation.ui.detail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.danieloliveira138.criptoviewer.domain.model.CoinItem
import com.danieloliveira138.criptoviewer.domain.model.ExchangeInfo
import com.danieloliveira138.criptoviewer.domain.model.ExchangeUrl
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class DetailListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun exchangeDetails_displaysCorrectInfo() {
        val exchangeInfo = ExchangeInfo(
            id = 1,
            name = "Binance",
            slug = "binance",
            logo = "logo_url",
            description = "The world's leading cryptocurrency exchange.",
            dateLaunched = "2017-07-14",
            urls = ExchangeUrl(website = listOf("https://www.binance.com"), null, null),
            makerFee = 0.1,
            takerFee = 0.1
        )
        val assets = listOf(
            CoinItem(name = "Bitcoin", price = "$60,000")
        )
        val state = DetailListState(
            isLoading = false,
            exchangeInfo = exchangeInfo,
            exchangeAssets = assets
        )

        composeTestRule.setContent {
            ExchangeDetailsContent(
                state = state,
                onEvent = {}
            )
        }

        composeTestRule.onNodeWithText("Binance").assertIsDisplayed()
        composeTestRule.onNodeWithText("The world's leading cryptocurrency exchange.").assertIsDisplayed()
        composeTestRule.onNodeWithText("Bitcoin").assertIsDisplayed()
        composeTestRule.onNodeWithText("$60,000").assertIsDisplayed()
    }

    @Test
    fun backButtonClick_triggersEvent() {
        val onEvent: (DetailListEvent) -> Unit = mock()

        composeTestRule.setContent {
            ExchangeDetailsContent(
                state = DetailListState(),
                onEvent = onEvent
            )
        }

        composeTestRule.onNodeWithContentDescription("Back").performClick()

        verify(onEvent).invoke(DetailListEvent.OnBackClicked)
    }

    @Test
    fun linkClick_triggersEvent() {
        val exchangeInfo = ExchangeInfo(
            id = 1,
            name = "Binance",
            slug = "binance",
            logo = "logo_url",
            description = "Description",
            dateLaunched = "2017",
            urls = ExchangeUrl(website = listOf("https://www.binance.com"), null, null),
            makerFee = 0.1,
            takerFee = 0.1
        )
        val state = DetailListState(exchangeInfo = exchangeInfo)
        val onEvent: (DetailListEvent) -> Unit = mock()

        composeTestRule.setContent {
            ExchangeDetailsContent(
                state = state,
                onEvent = onEvent
            )
        }

        composeTestRule.onNodeWithText("https://www.binance.com ↗").performClick()

        verify(onEvent).invoke(DetailListEvent.OnLinkClicked("https://www.binance.com"))
    }
}
