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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.danieloliveira138.criptoviewer.domain.model.ExchangeItem
import com.danieloliveira138.criptoviewer.ui.theme.BgDark
import com.danieloliveira138.criptoviewer.ui.theme.CardBg
import com.danieloliveira138.criptoviewer.ui.theme.Orange
import com.danieloliveira138.criptoviewer.ui.theme.TextDisabled
import com.danieloliveira138.criptoviewer.ui.theme.TextPrimary

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
            .background(BgDark),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BgDark)
                    .padding(start = 24.dp, end = 24.dp, top = 40.dp, bottom = 20.dp),
            ) {
                Text(
                    text = "Exchanges",
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Orange, thickness = 2.dp)
            }

            PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = { onEvent(MainListEvent.Refresh) },
                modifier = Modifier.fillMaxSize(),
            ) {
                LazyColumn(
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
                }
            }
        }

        // Full-screen loading overlay shown only during the initial fetch
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BgDark),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = Orange)
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
            .background(CardBg),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Left accent strip (visible for active, transparent for inactive)
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .background(if (isActive) Orange else Color.Transparent),
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
                color = if (isActive) TextPrimary else TextDisabled,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isActive) Orange.copy(alpha = 0.15f)
                        else TextDisabled.copy(alpha = 0.15f),
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Text(
                    text = if (isActive) "ACTIVE" else "OFFLINE",
                    color = if (isActive) Orange else TextDisabled,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
