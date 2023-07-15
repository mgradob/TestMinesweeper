package com.mgb.codingchallenge.minesweeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mgb.codingchallenge.minesweeper.ui.theme.MinesweeperTheme

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
                    Game()
                }
            }
        }
    }
}

@Composable
fun Game(
    vm: GameViewModel = viewModel()
) {
    val time = vm.timer.collectAsState()
    val boardState = vm.boardState.collectAsState()
    val gameStatus = vm.gameStatus.collectAsState()

    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Timer(time = time.value)

        Spacer(modifier = Modifier.height(16.dp))

        BoardView(boardModel = boardState.value)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = vm::startGame) {
            Text(text = "Iniciar")
        }
    }
}

@Composable
fun Timer(time: Int) {
    Box(
        modifier = Modifier
            .background(Color.Black)
            .padding(8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = String.format("%03d", time), color = Color.Red)
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun GreetingPreview() {
    MinesweeperTheme {
        Game()
    }
}


