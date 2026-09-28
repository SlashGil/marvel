package com.slashgil.marvel.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slashgil.marvel.domain.model.Powerstats
import com.slashgil.marvel.ui.theme.BgSurface
import com.slashgil.marvel.ui.theme.MarvelRed

@Composable
fun PowerstatsBarChart(
    powerstats: Powerstats,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatBar(label = "Intelligence", value = powerstats.intelligence, color = Color(0xFF38BDF8))
        StatBar(label = "Strength", value = powerstats.strength, color = MarvelRed)
        StatBar(label = "Speed", value = powerstats.speed, color = Color(0xFFFACC15))
        StatBar(label = "Durability", value = powerstats.durability, color = Color(0xFF4ADE80))
        StatBar(label = "Power", value = powerstats.power, color = Color(0xFFA855F7))
        StatBar(label = "Combat", value = powerstats.combat, color = Color(0xFFFB923C))
    }
}

@Composable
private fun StatBar(
    label: String,
    value: Int,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.width(90.dp)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(BgSurface)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = (value.coerceIn(0, 100) / 100f))
                    .clip(RoundedCornerShape(5.dp))
                    .background(color)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "$value",
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(30.dp)
        )
    }
}
