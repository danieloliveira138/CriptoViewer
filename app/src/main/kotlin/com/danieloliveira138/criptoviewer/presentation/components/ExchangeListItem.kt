package com.danieloliveira138.criptoviewer.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danieloliveira138.criptoviewer.domain.model.ExchangeItem
import com.danieloliveira138.criptoviewer.ui.theme.AppColor

@Composable
fun ExchangeItem(
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
