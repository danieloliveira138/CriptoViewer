package com.danieloliveira138.criptoviewer.presentation.ui.detailList

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.danieloliveira138.criptoviewer.domain.model.CoinItem

// Color Palette
val MbBgDark = Color(0xFF121214)
val MbCardBg = Color(0xFF202024)
val MbOrange = Color(0xFFF7931A)
val MbTextPrimary = Color(0xFFFFFFFF)
val MbTextSecondary = Color(0xFFA9A9B2)
val MbTextDisabled = Color(0xFF7C7C8A)
val MbGreenAccent = Color(0xFF00D395)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeDetailsScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Exchange Details", color = MbTextPrimary, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: Handle back */ }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MbOrange
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MbBgDark,
                    titleContentColor = MbTextPrimary
                )
            )
        },
        containerColor = MbBgDark
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            ExchangeHeader()
            StatsGrid()
            LinkRow()
            DescriptionSection(
                description = "Kraken is a US-based cryptocurrency exchange where users can buy, sell and trade various assets with relatively low commissions. Clients can also earn rewards through coin staking. The exchange has a leading level euro volume and liquidity..."
            )
            TradeCoinsSection(
                listOf(
                    CoinItem("Bitcoin (BTC)", "$62,450.00"),
                    CoinItem("Ethereum (ETH)", "$3,420.15"),
                    CoinItem("Solana (SOL)", "$145.30")
                )
            )
        }
    }
}

@Composable
fun ExchangeHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            model = "https://s2.coinmarketcap.com/static/img/exchanges/64x64/24.png",
            contentDescription = "Kraken Logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .padding(4.dp)
        )
        Column {
            Text(
                text = "Kraken",
                color = MbTextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "ID: 24 | Launched: Jul 2011",
                color = MbTextDisabled,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun StatsGrid() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(label = "Maker Fee", value = "0.02%", modifier = Modifier.weight(1f))
        StatCard(label = "Taker Fee", value = "0.05%", modifier = Modifier.weight(1f))
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MbCardBg
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .fillMaxHeight()
                    .background(MbOrange)
            )
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = label.uppercase(),
                    color = MbTextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = value,
                    color = MbTextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun LinkRow() {
    Column {
        Text(
            text = "Website",
            color = MbOrange,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MbCardBg,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "www.kraken.com ↗",
                        color = MbOrange,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable { /* Handle link click */ }
                    )
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
            color = MbOrange,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MbCardBg,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = description,
                color = MbTextSecondary,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun TradeCoinsSection(coinList: List<CoinItem>) {
    Column {
        Text(
            text = "Trade Coins",
            color = MbOrange,
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
        color = MbCardBg,
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
                color = MbTextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = coin.price,
                color = MbGreenAccent,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewExchangeDetailsScreen() {
    MaterialTheme {
        ExchangeDetailsScreen()
    }
}