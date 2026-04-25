package com.danieloliveira138.criptoviewer.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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