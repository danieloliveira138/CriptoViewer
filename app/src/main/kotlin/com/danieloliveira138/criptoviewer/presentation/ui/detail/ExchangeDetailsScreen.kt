package com.danieloliveira138.criptoviewer.presentation.ui.detail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.danieloliveira138.criptoviewer.core.utils.toNormalizedString
import com.danieloliveira138.criptoviewer.presentation.components.DescriptionSection
import com.danieloliveira138.criptoviewer.presentation.components.ExchangeHeader
import com.danieloliveira138.criptoviewer.presentation.components.LinkRow
import com.danieloliveira138.criptoviewer.presentation.components.StatsGrid
import com.danieloliveira138.criptoviewer.presentation.components.TradeCoinsList
import com.danieloliveira138.criptoviewer.ui.theme.AppColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeDetailsScreen(
    viewModel: ExchangeDetailsViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsState()
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ExchangeDetailsEffect.navigateToBrowser -> {
                    val url = effect.url.toNormalizedString()
                    uriHandler.openUri(url)
                }
                is ExchangeDetailsEffect.navigateBack ->
                    navController.popBackStack()
                is ExchangeDetailsEffect.showToast ->
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    ExchangeDetailsContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeDetailsContent(
    state: ExchangeDetailsState?,
    onEvent: (ExchangeDetailsEvent) -> Unit = {}
) {
    val exchangeInfo = state?.exchangeInfo

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Exchange Details",
                        color = AppColor.TextPrimary,
                        fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = { onEvent(ExchangeDetailsEvent.OnBackClicked) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = AppColor.Orange
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColor.BgDark,
                    titleContentColor = AppColor.BgDark
                )
            )
        },
        containerColor = AppColor.BgDark
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            ExchangeHeader(
                exchangeName = exchangeInfo?.name,
                imageUrl = state?.exchangeInfo?.logo,
                subTitle = "ID: ${state?.exchangeInfo?.id} | Launched: ${state?.exchangeInfo?.dateLaunched}"
            )
            StatsGrid(
                makerFee = "${exchangeInfo?.makerFee}%",
                takerFee = "${exchangeInfo?.takerFee}%"

            )
            LinkRow(
                exchangeLinks = state?.exchangeInfo?.urls?.website ?: listOf("-----"),
                onLinkClicked = { url -> onEvent(ExchangeDetailsEvent.OnLinkClicked(url)) }
            )
            DescriptionSection(
                description = exchangeInfo?.description ?: "-----"
            )
            TradeCoinsList(
                state?.exchangeAssets ?: emptyList()
            )
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

@Preview(showBackground = true)
@Composable
fun PreviewExchangeDetailsScreen() {
    ExchangeDetailsContent(null) { }
}