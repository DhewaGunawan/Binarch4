package com.binar.binarch4.manager

import android.util.Log
import com.binar.binarch4.enum.GameState
import com.binar.binarch4.enum.PlayerSide
import com.binar.binarch4.enum.PlayerState
import com.binar.binarch4.enum.PlayerType
import com.binar.binarch4.model.Player
import kotlin.random.Random

interface RPSGameManager {
    fun initGame()
    fun movePlayerOne(type: String)
    fun startGame()
    fun resetGame()
}

interface RPSGameListener {
    fun onPlayerStatusChanged(player: Player)
    fun onResetImage()
    fun onGameStateChanged(gameState: GameState)
    fun onGameFinished(gameState: GameState, winner: Player)
}

class RPSComputerEnemyGameManager(
    private val listener: RPSGameListener
) : RPSGameManager {

    private lateinit var playerOne: Player

    private lateinit var playerTwo: Player

    private lateinit var draw: Player

    private lateinit var gameState: GameState

    override fun initGame() {
        setGameState(GameState.IDLE)
        playerOne = Player(PlayerSide.PLAYER_ONE, PlayerState.IDLE, PlayerType.PAPER)
        playerTwo = Player(PlayerSide.PLAYER_TWO, PlayerState.IDLE, PlayerType.PAPER)
        draw = Player(PlayerSide.DRAW, PlayerState.IDLE, PlayerType.PAPER)
        notifyPlayerDataChanged()
        setGameState(GameState.STARTED)
    }

    private fun notifyPlayerDataChanged() {
        if (playerOne.playerState == PlayerState.IDLE) {
            listener.onResetImage()
        }
        listener.onPlayerStatusChanged(
            playerTwo
        )
    }

    override fun movePlayerOne(type: String) {
        Log.d("Testing4", "movePlayerOne: $type")
        when (type) {
            "ROCK" -> setPlayerOneMovement(getPlayerTypeByOrdinal(0), PlayerState.CHOOSED)
            "PAPER" -> setPlayerOneMovement(getPlayerTypeByOrdinal(1), PlayerState.CHOOSED)
            "SCISSORS" -> setPlayerOneMovement(getPlayerTypeByOrdinal(2), PlayerState.CHOOSED)
        }
    }


    private fun setPlayerOneMovement(
        playerType: PlayerType = playerOne.playerType,
        playerState: PlayerState = playerOne.playerState
    ) {
        playerOne.apply {
            this.playerType = playerType
            this.playerState = playerState
        }
    }

    private fun setPlayerTwoMovement(
        playerPosition: PlayerType = playerTwo.playerType,
        playerState: PlayerState = playerTwo.playerState
    ) {
        playerTwo.apply {
            this.playerType = playerPosition
            this.playerState = playerState
        }
        listener.onPlayerStatusChanged(
            playerTwo
        )
    }

    private fun getPlayerTypeByOrdinal(index: Int): PlayerType {
        return PlayerType.values()[index]
    }

    private fun setGameState(newGameState: GameState) {
        gameState = newGameState
        listener.onGameStateChanged(gameState)
    }

    override fun startGame() {
        playerTwo.apply {
            playerType = getPlayerTwoType()
        }
        checkPlayerWinner()
    }

    override fun resetGame() {
        initGame()
    }

    private fun checkPlayerWinner() {
        val winner = when {
            playerOne.playerType.ordinal == playerTwo.playerType.ordinal -> {
                setPlayerTwoMovement(playerOne.playerType, PlayerState.CHOOSED)
                draw
            }
            playerOne.playerType.ordinal == 2 && playerTwo.playerType.ordinal == 1 -> {
                setPlayerTwoMovement(PlayerType.PAPER, PlayerState.CHOOSED)
                playerOne
            }
            playerOne.playerType.ordinal == 2 && playerTwo.playerType.ordinal == 0 -> {
                setPlayerTwoMovement(PlayerType.ROCK, PlayerState.CHOOSED)
                playerTwo
            }
            playerOne.playerType.ordinal == 1 && playerTwo.playerType.ordinal == 2 -> {
                setPlayerTwoMovement(PlayerType.SCISSORS, PlayerState.CHOOSED)
                playerTwo
            }
            playerOne.playerType.ordinal == 1 && playerTwo.playerType.ordinal == 0 -> {
                setPlayerTwoMovement(PlayerType.ROCK, PlayerState.CHOOSED)
                playerOne
            }
            playerOne.playerType.ordinal == 0 && playerTwo.playerType.ordinal == 2 -> {
                setPlayerTwoMovement(PlayerType.SCISSORS, PlayerState.CHOOSED)
                playerOne
            }
            playerOne.playerType.ordinal == 0 && playerTwo.playerType.ordinal == 1 -> {
                setPlayerTwoMovement(PlayerType.PAPER, PlayerState.CHOOSED)
                playerTwo
            }
            else -> {
                draw
            }
        }
        setGameState(GameState.FINISHED)
        listener.onGameFinished(gameState, winner)
    }

    private fun getPlayerTwoType(): PlayerType {
        val randomPosition = Random.nextInt(0, until = PlayerType.values().size)
        return getPlayerTypeByOrdinal(randomPosition)
    }
}