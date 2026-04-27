package com.danieloliveira138.criptoviewer.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.danieloliveira138.criptoviewer.ui.theme.AppColor


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
