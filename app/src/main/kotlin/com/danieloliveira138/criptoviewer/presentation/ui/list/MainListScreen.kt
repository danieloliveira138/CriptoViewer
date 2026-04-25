package com.danieloliveira138.criptoviewer.presentation.ui.list

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.danieloliveira138.criptoviewer.domain.model.ExchangeItem
import com.danieloliveira138.criptoviewer.presentation.components.ExchangeItem
import com.danieloliveira138.criptoviewer.ui.theme.AppColor

private const val LOAD_MORE_THRESHOLD = 5

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainListScreen(
    navController: NavController,
    viewModel: MainListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MainListEffect.NavigateTo -> navController.navigate(effect.route)
                is MainListEffect.ShowToast ->
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    MainListContent(
        state = state,
        onEvent = viewModel::onEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainListContent(
    state: MainListState? = null,
    onEvent: (MainListEvent) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.BgDark),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppColor.BgDark)
                    .padding(start = 24.dp, end = 24.dp, top = 40.dp, bottom = 20.dp),
            ) {
                Text(
                    text = "Exchanges",
                    color = AppColor.TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = AppColor.Orange, thickness = 2.dp)
            }

            val listState = rememberLazyListState()
            val shouldLoadMore by remember {
                derivedStateOf {
                    val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                        ?: return@derivedStateOf false
                    lastVisible.index >= listState.layoutInfo.totalItemsCount - LOAD_MORE_THRESHOLD
                }
            }

            LaunchedEffect(shouldLoadMore) {
                if (shouldLoadMore) onEvent(MainListEvent.LoadNextPage)
            }

            PullToRefreshBox(
                isRefreshing = state?.isRefreshing ?: false,
                onRefresh = { onEvent(MainListEvent.Refresh) },
                modifier = Modifier.fillMaxSize(),
            ) {
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(
                        items = state?.exchanges ?: emptyList(),
                        key = { it.id }
                    ) { exchange ->
                        ExchangeItem(
                            exchange = exchange,
                            onClick = { onEvent(MainListEvent.OnExchangeClick(exchange)) },
                        )
                    }

                    if (state?.isLoadingMore == true) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                CircularProgressIndicator(color = AppColor.Orange)
                            }
                        }
                    }
                }
            }
        }

        if (state?.isLoading == true) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppColor.BgDark),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = AppColor.Orange)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    val exchanges = listOf(
        ExchangeItem(id = 1, name = "Mercado Bitcoin", isActive = true),
        ExchangeItem(id = 2, name = "Kraken", isActive = false),
        ExchangeItem(id = 3, name = "Poloniex", isActive = true),
        ExchangeItem(id = 4, name = "Bittylicius", isActive = true),
        ExchangeItem(id = 5, name = "Cex.IO", isActive = true),
        ExchangeItem(id = 6, name = "HitBTC", isActive = true),
        ExchangeItem(id = 7, name = "EXMO", isActive = true),
        ExchangeItem(id = 8, name = "Okcoin", isActive = true),
        ExchangeItem(id = 9, name = "Indodax", isActive = true),
        ExchangeItem(id = 10, name = "Zaif", isActive = true)
    )

    MainListContent(state =
        MainListState(
            isLoading = false,
            isRefreshing = false,
            isLoadingMore = false,
            exchanges = exchanges,
            currentPage = 1,
            hasMorePages = true,
            error = null,
        )
    ) { }
}
