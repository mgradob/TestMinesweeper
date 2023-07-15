package com.mgb.codingchallenge.minesweeper

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.lang.StringBuilder
import kotlin.random.Random
import kotlin.random.asJavaRandom

enum class CellStatus {
    OPEN, CLOSED
}

data class CellModel(
    val hasBomb: Boolean = false,
    val flagged: Boolean = false,
    val neighborBombs: Int = 0,
    val cellStatus: CellStatus = CellStatus.CLOSED,
) {
    @Composable
    fun GetCell() {
        if (cellStatus == CellStatus.CLOSED) {
            if (flagged) return FlagCell()

            return ClosedCell()
        } else {
            if (hasBomb) return BombCell()

            return OpenCell()
        }
    }
}

data class BoardModel(
    val grid: List<List<CellModel>>
)

sealed class GameConfig(
    val x: Int = 0,
    val y: Int = 0,
    val bombs: Int = 0,
) {
    object Easy : GameConfig(9, 9, 10)
    object Medium : GameConfig(16, 16, 40)
    object Hard : GameConfig(16, 30, 99)
}

enum class GameStatus {
    NEW, RUNNING, WIN, LOSS
}

class GameViewModel : ViewModel() {
    var gameStatus = MutableStateFlow(GameStatus.NEW)
        private set
    var timer = MutableStateFlow(0)
        private set
    var boardState = MutableStateFlow(generateBoard())
        private set

    var job: Job? = null

    fun startGame() {
        gameStatus.value = GameStatus.RUNNING

        boardState.value = generateBoard()

        //startTimer()
    }

    private fun generateBoard(): BoardModel {
        // Hacemos nueva config
        val config = GameConfig.Easy

        // Generamos nueva grid
        val grid = generateGrid(config.x, config.y)

        // Agregamos bombas
        addBombs(grid, config.bombs)

        // Seteamos state del vm
        return BoardModel(grid)
    }

    private fun generateGrid(x: Int, y: Int): MutableList<MutableList<CellModel>> {
        val grid = mutableListOf<MutableList<CellModel>>()

        for (row in 0 until x) {
            val r = mutableListOf<CellModel>()

            for (col in 0 until y) {
                r.add(CellModel())
            }

            grid.add(r)
        }

        return grid
    }

    private fun addBombs(grid: MutableList<MutableList<CellModel>>, bombs: Int) {
        val gridX = grid.first().size
        val gridY = grid.size

        var remainingBombs = bombs

        val random = Random(100000)

        println("Grid antes:")
        printGrid(grid)

        do {
            println("remainingBombs: $remainingBombs")

            // Generas una posicion para la bomba random
            val bombX = random.nextInt(0, gridX)
            val bombY = random.nextInt(0, gridY)

            println("bombX: $bombX")
            println("bombY: $bombY")

            val cell = grid[bombX][bombY]
            println("cell: $cell")

            // Checas si ya tiene bomba la posicion
            if (cell.hasBomb) continue

            // Poner la bomba en la celda
            val newCell = cell.copy(hasBomb = true, cellStatus = CellStatus.OPEN)
            println("newCell: $newCell")

            grid[bombX][bombY] = newCell

            // Agregar los numeros a las celdas vecinas
            //addNeighbors(grid, bombX, bombY)

            remainingBombs--
        } while (remainingBombs > 0)

        println("Grid despues:")
        printGrid(grid)
    }

    private fun addNeighbors(grid: MutableList<MutableList<CellModel>>, bombX: Int, bombY: Int) {
        val gridX = grid.first().size
        val gridY = grid.size

        for (x in -1..1) {
            val nX = bombX + x
            if (nX < 0 || nX >= gridX) continue

            for (y in -1..1) {
                val nY = bombY + y
                if (nY < 0 || nY >= gridY) continue
                if (bombX == nX && bombY == nY) continue

                val cell = grid[nX][nY]
                grid[nX][nY] = cell.copy(neighborBombs = cell.neighborBombs + 1)
            }
        }
    }

    private fun startTimer() {
        job?.cancel()
        job = viewModelScope.launch {
            var count = 0

            while (true) {
                timer.value = count++

                delay(1000)
            }
        }
    }

    private fun printGrid(grid: MutableList<MutableList<CellModel>>) {
        val sb = StringBuilder()
        sb.append("|")

        grid.forEach { row ->
            sb.append("| ")

            row.forEach { cell ->
                sb.append(
                    when {
                        cell.hasBomb -> "X"
                        cell.flagged -> "F"
                        cell.neighborBombs > 0 -> cell.neighborBombs
                        else -> " "
                    }
                ).append("|")
            }

            sb.append("\n")
        }

        println(sb.toString())
    }
}
