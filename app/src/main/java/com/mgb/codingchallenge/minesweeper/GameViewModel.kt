package com.mgb.codingchallenge.minesweeper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgb.codingchallenge.minesweeper.widgets.BoardState
import com.mgb.codingchallenge.minesweeper.widgets.CellModel
import com.mgb.codingchallenge.minesweeper.widgets.CellState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

enum class GameStatus {
    NEW, RUNNING, WIN, LOSE
}

class GameViewModel : ViewModel() {
    var boardState = MutableStateFlow((BoardState(EasyConfig())))
        private set

    var gameStatus = MutableStateFlow(GameStatus.NEW)
        private set

    var time = MutableStateFlow(0)
        private set

    var remainingBombs = MutableStateFlow(0)
        private set

    private var timerJob: Job? = null

    fun startGame(gameConfig: GameConfig = EasyConfig()) {
        boardState.value = BoardState(gameConfig)

        gameStatus.value = GameStatus.RUNNING

        remainingBombs.value = boardState.value.gameConfig.bombs

        startTimer()
    }

    fun endGame(gameResult: GameStatus = GameStatus.NEW) {
        stopTimer()

        gameStatus.value = gameResult
    }

    fun onCellClick(cell: CellModel) {
        boardState.value.updateCell(cell.copy(state = CellState.OPEN))

        if (cell.hasBomb) {
            endGame((GameStatus.LOSE))
            return
        }

        openNeighbors(cell)

        checkIfWon()
    }

    fun onCellLongClick(cell: CellModel) {
        val flagged = !cell.flagged
        boardState.value.updateCell(cell.copy(flagged = flagged))

        remainingBombs.value = if (flagged) remainingBombs.value - 1 else remainingBombs.value + 1
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var count = 0

            while (true) {
                time.value = count++

                delay(1000)
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
    }

    private fun checkIfWon() {
        if (boardState.value.closedCells == boardState.value.gameConfig.bombs) {
            endGame(GameStatus.WIN)
        }
    }

    private fun openNeighbors(cell: CellModel) {
        val neighbors = boardState.value.getNeighbors(cell)
            .filterNot { it.hasBomb || it.flagged || it.state == CellState.OPEN }

        neighbors.forEach {
            boardState.value.updateCell(it.copy(state = CellState.OPEN))

            if (cell.neighborBombs == 0) {
                openNeighbors(it)
            }
        }
    }
}
