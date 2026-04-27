package com.danieloliveira138.criptoviewer.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danieloliveira138.criptoviewer.domain.model.CoinItem
import com.danieloliveira138.criptoviewer.ui.theme.AppColor
import kotlin.collections.forEach

@Composable
fun TradeCoinsList(coinList: List<CoinItem>) {
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
