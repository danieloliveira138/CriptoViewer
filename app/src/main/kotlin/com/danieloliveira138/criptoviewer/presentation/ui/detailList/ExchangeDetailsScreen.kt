package com.danieloliveira138.criptoviewer.presentation.ui.detailList

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.danieloliveira138.criptoviewer.core.utils.toNormalizedString
import com.danieloliveira138.criptoviewer.domain.model.CoinItem
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
                is ExchangeDetailEffect.navigateToBrowser -> {
                    val url = effect.url.toNormalizedString()
                    uriHandler.openUri(url)
                }
                is ExchangeDetailEffect.navigateBack ->
                    navController.popBackStack()
                is ExchangeDetailEffect.showToast ->
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
    state: DetailListState?,
    onEvent: (DetailListEvent) -> Unit = {}
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
                    IconButton(onClick = { onEvent(DetailListEvent.OnBackClicked) }) {
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
                onLinkClicked = { url -> onEvent(DetailListEvent.OnLinkClicked(url)) }
            )
            DescriptionSection(
                description = exchangeInfo?.description ?: "-----"
            )
            TradeCoinsSection(
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

@Composable
fun ExchangeHeader(
    exchangeName: String?,
    imageUrl: String?,
    subTitle: String?
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Exchange Logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .padding(4.dp)
        )
        Column {
            Text(
                text = exchangeName ?: "-----",
                color = AppColor.TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subTitle ?: "-----",
                color = AppColor.TextDisabled,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun StatsGrid(
    makerFee: String,
    takerFee: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(label = "Maker Fee", value = makerFee, modifier = Modifier.weight(1f))
        StatCard(label = "Taker Fee", value = takerFee, modifier = Modifier.weight(1f))
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = AppColor.CardBg
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .fillMaxHeight()
                    .background(AppColor.CardBg)
            )
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = label.uppercase(),
                    color = AppColor.TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = value,
                    color = AppColor.TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun LinkRow(
    exchangeLinks: List<String>,
    onLinkClicked: (String) -> Unit
) {
    Column {
        Text(
            text = "Website",
            color = AppColor.Orange,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = AppColor.CardBg,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                exchangeLinks.forEach {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$it ↗",
                            color = AppColor.Orange,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable { onLinkClicked.invoke(it) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DescriptionSection(
    description: String
) {
    Column {
        Text(
            text = "About",
            color = AppColor.Orange,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = AppColor.CardBg,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = description,
                color = AppColor.TextSecondary,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun TradeCoinsSection(coinList: List<CoinItem>) {
    if (coinList.isEmpty()) return
    Column {
        Text(
            text = "Trade Coins",
            color = AppColor.Orange,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            coinList.forEach { coin ->
                CoinItem(coin)
            }
        }
    }
}

@Composable
fun CoinItem(coin: CoinItem) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = AppColor.CardBg,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = coin.name,
                color = AppColor.TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = coin.price,
                color = AppColor.GreenAccent,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewExchangeDetailsScreen() {
    ExchangeDetailsContent(null) { }
}