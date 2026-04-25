package com.danieloliveira138.criptoviewer.presentation.ui.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.danieloliveira138.criptoviewer.domain.model.ExchangeItem
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class MainListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingState_showsHeader() {
        val state = MainListState(isLoading = true)
        
        composeTestRule.setContent {
            MainListContent(
                state = state,
                onEvent = {}
            )
        }

        composeTestRule.onNodeWithText("Exchanges").assertIsDisplayed()
    }

    @Test
    fun exchangeList_displaysItems() {
        val exchanges = listOf(
            ExchangeItem(id = 1, name = "Binance", isActive = true),
            ExchangeItem(id = 2, name = "Coinbase", isActive = true)
        )
        val state = MainListState(exchanges = exchanges)

        composeTestRule.setContent {
            MainListContent(
                state = state,
                onEvent = {}
            )
        }

        composeTestRule.onNodeWithText("Binance").assertIsDisplayed()
        composeTestRule.onNodeWithText("Coinbase").assertIsDisplayed()
    }

    @Test
    fun exchangeItemClick_triggersEvent() {
        val exchange = ExchangeItem(id = 1, name = "Binance", isActive = true)
        val state = MainListState(exchanges = listOf(exchange))
        val onEvent: (MainListEvent) -> Unit = mock()

        composeTestRule.setContent {
            MainListContent(
                state = state,
                onEvent = onEvent
            )
        }

        composeTestRule.onNodeWithText("Binance").performClick()

        verify(onEvent).invoke(MainListEvent.OnExchangeClick(exchange))
    }
}
