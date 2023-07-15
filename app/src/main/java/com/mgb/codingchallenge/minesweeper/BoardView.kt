package com.mgb.codingchallenge.minesweeper

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BoardView(boardModel: BoardModel) {
    Column(
        modifier = Modifier.border(1.dp, Color.Black).fillMaxHeight(0.8F).fillMaxWidth()
    ) {
        boardModel.grid.forEach { cellModels ->
            Row {
                cellModels.forEach { cellModel ->
                    cellModel.GetCell()
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun PreviewBoardView() {
    val config = GameConfig.Easy

    val rows = mutableListOf<MutableList<CellModel>>()
    repeat(config.x) {
        val col = mutableListOf<CellModel>()
        repeat(config.y) {
            col.add(CellModel())
        }

        rows.add(col)
    }

    Box(
        modifier = Modifier.padding(16.dp)
    ) {
        BoardView(
            BoardModel(rows)
        )
    }
}
