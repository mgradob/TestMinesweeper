package com.mgb.codingchallenge.minesweeper

sealed class GameConfig(val x: Int, val y: Int, val bombs: Int)

class EasyConfig : GameConfig(9, 9, 10)

class MediumConfig : GameConfig(16, 16, 40)

class HardConfig : GameConfig(16, 30, 99)