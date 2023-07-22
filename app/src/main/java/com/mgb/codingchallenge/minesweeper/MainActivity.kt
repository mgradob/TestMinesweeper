package com.mgb.codingchallenge.minesweeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mgb.codingchallenge.minesweeper.ui.theme.MinesweeperTheme
import com.mgb.codingchallenge.minesweeper.widgets.BoardView
import com.mgb.codingchallenge.minesweeper.widgets.BombsCounter
import com.mgb.codingchallenge.minesweeper.widgets.Timer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MinesweeperTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GameView()
                }
            }
        }
    }
}

@Composable
fun GameView(
    vm: GameViewModel = viewModel()
) {
    val time by vm.time.collectAsState()
    val remainingBombs by vm.remainingBombs.collectAsState()
    val boardState by vm.boardState.collectAsState()
    val gameStatus by vm.gameStatus.collectAsState()

    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (gameStatus == GameStatus.WIN || gameStatus == GameStatus.LOSE) {
            Text(
                text = if (gameStatus == GameStatus.WIN) "WIN" else "LOSE",
                color = if (gameStatus == GameStatus.WIN) Color.Green else Color.Red,
                fontSize = 40.sp,
                fontWeight = FontWeight.Black,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Timer(time = time)

            BombsCounter(remainingBombs)
        }

        Spacer(modifier = Modifier.height(16.dp))

        BoardView(
            state = boardState,
            onCellClick = if (gameStatus == GameStatus.RUNNING) vm::onCellClick else { _ -> },
            onCellLongClick = if (gameStatus == GameStatus.RUNNING) vm::onCellLongClick else { _ -> },
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (gameStatus != GameStatus.RUNNING) {
            Button(onClick = vm::startGame) {
                Text(text = "Easy")
            }

            Button(onClick = { vm.startGame(MediumConfig()) }) {
                Text(text = "Medium")
            }

            Button(onClick = { vm.startGame(HardConfig()) }) {
                Text(text = "Hard")
            }
        } else {
            Button(onClick = vm::endGame) {
                Text(text = "End game")
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun PreviewGame() {
    MinesweeperTheme {
        GameView()
    }
}
