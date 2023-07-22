package com.mgb.codingchallenge.minesweeper.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.mgb.codingchallenge.minesweeper.EasyConfig
import com.mgb.codingchallenge.minesweeper.GameConfig
import kotlin.random.Random

class BoardState(val gameConfig: GameConfig) {
    val x = gameConfig.x
    val y = gameConfig.y

    val grid: MutableList<MutableList<CellModel>>

    var closedCells: Int
        private set

    init {
        grid = generateNewGrid(x, y)
        populateBombs()
        printGrid()

        closedCells = x * y
    }

    fun updateCell(cell: CellModel) {
        if (cell.state == CellState.OPEN && grid[cell.x][cell.y].state == CellState.CLOSED) {
            closedCells--
        }

        grid[cell.x][cell.y] = cell
    }

    fun getNeighbors(cell: CellModel): MutableList<CellModel> {
        val neighbors = mutableListOf<CellModel>()

        grid.getOrNull(cell.x - 1)?.getOrNull(cell.y - 1)?.let { neighbors.add(it) }
        grid.getOrNull(cell.x - 1)?.getOrNull(cell.y)?.let { neighbors.add(it) }
        grid.getOrNull(cell.x - 1)?.getOrNull(cell.y + 1)?.let { neighbors.add(it) }
        grid.getOrNull(cell.x)?.getOrNull(cell.y - 1)?.let { neighbors.add(it) }
        grid.getOrNull(cell.x)?.getOrNull(cell.y + 1)?.let { neighbors.add(it) }
        grid.getOrNull(cell.x + 1)?.getOrNull(cell.y - 1)?.let { neighbors.add(it) }
        grid.getOrNull(cell.x + 1)?.getOrNull(cell.y)?.let { neighbors.add(it) }
        grid.getOrNull(cell.x + 1)?.getOrNull(cell.y + 1)?.let { neighbors.add(it) }

        return neighbors
    }

    private fun generateNewGrid(x: Int, y: Int): MutableList<MutableList<CellModel>> {
        val grid = mutableListOf<MutableList<CellModel>>()

        for (r in 0 until x) {
            val row = mutableListOf<CellModel>()

            for (c in 0 until y) {
                row.add(CellModel(r, c))
            }

            grid.add(row)
        }

        return grid
    }

    private fun populateBombs(): MutableList<MutableList<CellModel>> {
        var remainingBombs = gameConfig.bombs

        do {
            val bombX = Random.nextInt(0, x)
            val bombY = Random.nextInt(0, y)

            val cell = grid[bombX][bombY]
            if (!cell.hasBomb) {
                grid[bombX][bombY] = cell.copy(hasBomb = true)

                populateNeighbors(cell)

                remainingBombs--
            }
        } while (remainingBombs > 0)

        return grid
    }

    private fun populateNeighbors(cell: CellModel) {
        val neighbors = getNeighbors(cell)
        neighbors.forEach {
            grid[it.x][it.y] = it.copy(neighborBombs = it.neighborBombs + 1)
        }
    }

    private fun printGrid() {
        val sb = StringBuilder()

        grid.forEach { row ->
            row.forEach { cell ->
                sb.append("|")
                    .append(
                        when {
                            cell.hasBomb -> "X"
                            cell.neighborBombs > 0 -> "${cell.neighborBombs}"
                            else -> " "
                        }
                    )
            }

            sb.append("|\n")
        }

        println(sb.toString())
    }
}

@Composable
fun BoardView(
    state: BoardState,
    onCellClick: (CellModel) -> Unit,
    onCellLongClick: (CellModel) -> Unit,
) {
    Column {
        for (row in 0 until state.x) {
            Row {
                for (col in 0 until state.y) {
                    CellView(
                        cellModel = state.grid[row][col],
                        onClick = onCellClick,
                        onLongClick = onCellLongClick,
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun PreviewBoard() {
    Box(contentAlignment = Alignment.Center) {
        BoardView(state = BoardState(EasyConfig()), onCellClick = {}, onCellLongClick = {})
    }
}
