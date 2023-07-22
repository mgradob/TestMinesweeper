package com.mgb.codingchallenge.minesweeper.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Timer(time: Int) {
    Box(
        modifier = Modifier
            .size(50.dp, 30.dp)
            .background(Color.Black)
            .border(1.dp, Color.LightGray),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = if (time > 999) "999" else String.format("%03d", time),
            color = Color.Red,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
@Preview
private fun PreviewTimer() {
    Column {
        Timer(1)

        Timer(11)

        Timer(111)

        Timer(1111)
    }
}