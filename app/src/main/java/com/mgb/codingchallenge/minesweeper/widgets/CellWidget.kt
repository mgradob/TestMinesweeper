package com.mgb.codingchallenge.minesweeper.widgets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mgb.codingchallenge.minesweeper.R

enum class CellState {
    CLOSED, OPEN
}

data class CellModel(
    val x: Int,
    val y: Int,
    val hasBomb: Boolean = false,
    val neighborBombs: Int = 0,
    val flagged: Boolean = false,
    val state: CellState = CellState.CLOSED,
)

private val cellModifier: Modifier = Modifier
    .size(40.dp)
    .border(1.dp, Color.Black)

private const val lineOffset = 4F

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CellView(
    cellModel: CellModel,
    onClick: (CellModel) -> Unit,
    onLongClick: (CellModel) -> Unit,
) {
    var modifier = when (cellModel.state) {
        CellState.CLOSED -> {
            cellModifier
                .background(Color.LightGray)
                .drawBehind {
                    drawLine(
                        color = Color.Gray,
                        start = Offset(lineOffset, lineOffset),
                        end = Offset(size.width - lineOffset, lineOffset),
                        strokeWidth = 4.dp.toPx(),
                    )
                }
                .drawBehind {
                    drawLine(
                        color = Color.Gray,
                        start = Offset(lineOffset, lineOffset),
                        end = Offset(lineOffset, size.height - lineOffset),
                        strokeWidth = 4.dp.toPx(),
                    )
                }
                .drawBehind {
                    drawLine(
                        color = Color.Gray,
                        start = Offset(size.width - lineOffset, lineOffset),
                        end = Offset(size.width - lineOffset, size.height - lineOffset),
                        strokeWidth = 4.dp.toPx(),
                    )
                }
                .drawBehind {
                    drawLine(
                        color = Color.Gray,
                        start = Offset(lineOffset, size.height - lineOffset),
                        end = Offset(size.width - lineOffset, size.height - lineOffset),
                        strokeWidth = 4.dp.toPx(),
                    )
                }
        }

        CellState.OPEN -> {
            cellModifier.background(Color.Gray)
        }
    }

    modifier = modifier.combinedClickable(
        enabled = cellModel.state == CellState.CLOSED,
        onClick = { onClick(cellModel) },
        onLongClick = { onLongClick(cellModel) }
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        when (cellModel.state) {
            CellState.CLOSED -> {
                if (cellModel.flagged) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_flag),
                        contentDescription = "Flag"
                    )
                }
            }

            CellState.OPEN -> {
                if (cellModel.hasBomb) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_bomb),
                        contentDescription = "Bomb"
                    )
                } else if (cellModel.neighborBombs > 0) {
                    Text(
                        text = "${cellModel.neighborBombs}",
                        color = textColor(cellModel.neighborBombs),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

private fun textColor(value: Int) = when (value) {
    1 -> Color.Blue
    2 -> Color.Green
    3 -> Color.Red
    4 -> Color.Blue.copy(blue = 0.5F)
    5 -> Color.Red.copy(red = 0.5F)
    6 -> Color.Cyan
    7 -> Color.Black
    8 -> Color.DarkGray
    else -> Color.Transparent
}

@Composable
@Preview
private fun PreviewCellWidget() {
    Column {
        CellView(cellModel = CellModel(0, 0), onClick = {}, onLongClick = {})

        for (x in 0..8) {
            CellView(
                cellModel = CellModel(0, 0, state = CellState.OPEN),
                onClick = {},
                onLongClick = {},
            )
        }

        CellView(cellModel = CellModel(0, 0, flagged = true), onClick = {}, onLongClick = {})

        CellView(
            cellModel = CellModel(0, 0, hasBomb = true, state = CellState.OPEN),
            onClick = {},
            onLongClick = {},
        )
    }
}
