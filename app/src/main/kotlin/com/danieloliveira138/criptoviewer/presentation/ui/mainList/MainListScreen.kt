package com.danieloliveira138.criptoviewer.presentation.ui.mainList

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.danieloliveira138.criptoviewer.domain.model.ExchangeItem
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
private fun MainListContent(
    state: MainListState,
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
                isRefreshing = state.isRefreshing,
                onRefresh = { onEvent(MainListEvent.Refresh) },
                modifier = Modifier.fillMaxSize(),
            ) {
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(items = state.exchanges, key = { it.id }) { exchange ->
                        ExchangeItem(
                            exchange = exchange,
                            onClick = { onEvent(MainListEvent.OnExchangeClick(exchange)) },
                        )
                    }

                    if (state.isLoadingMore) {
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

        // Full-screen loading overlay shown only during the initial fetch
        if (state.isLoading) {
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

@Composable
private fun ExchangeItem(
    exchange: ExchangeItem,
    onClick: () -> Unit,
) {
    val isActive = exchange.isActive

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (isActive) 1f else 0.5f)
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(12.dp))
            .then(if (isActive) Modifier.clickable(onClick = onClick) else Modifier)
            .background(AppColor.CardBg),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Left accent strip (visible for active, transparent for inactive)
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .background(if (isActive) AppColor.Orange else Color.Transparent),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = exchange.name,
                color = if (isActive) AppColor.TextPrimary else AppColor.TextDisabled,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isActive) AppColor.Orange.copy(alpha = 0.15f)
                        else AppColor.TextDisabled.copy(alpha = 0.15f),
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Text(
                    text = if (isActive) "ACTIVE" else "OFFLINE",
                    color = if (isActive) AppColor.Orange else AppColor.TextDisabled,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    ExchangeItem(exchange = ExchangeItem(id = 1, name = "Exchange 1", isActive = true)) {

    }
}
