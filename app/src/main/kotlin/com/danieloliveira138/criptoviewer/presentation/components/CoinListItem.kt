package com.danieloliveira138.criptoviewer.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danieloliveira138.criptoviewer.domain.model.CoinItem
import com.danieloliveira138.criptoviewer.ui.theme.AppColor

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