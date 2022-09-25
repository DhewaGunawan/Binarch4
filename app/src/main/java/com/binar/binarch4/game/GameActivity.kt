package com.binar.binarch4.game

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.binar.binarch4.R
import com.binar.binarch4.databinding.ActivityGameBinding
import com.binar.binarch4.enum.GameState
import com.binar.binarch4.enum.PlayerSide
import com.binar.binarch4.enum.PlayerState
import com.binar.binarch4.enum.PlayerType
import com.binar.binarch4.manager.RPSComputerEnemyGameManager
import com.binar.binarch4.manager.RPSGameListener
import com.binar.binarch4.manager.RPSGameManager
import com.binar.binarch4.model.Player

class GameActivity : AppCompatActivity(), RPSGameListener {
    private val binding: ActivityGameBinding by lazy {
        ActivityGameBinding.inflate(layoutInflater)
    }

    private val rpsGameManager: RPSGameManager by lazy {
        RPSComputerEnemyGameManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        rpsGameManager.initGame()
        setOnClickListeners()
        supportActionBar?.hide()
    }

    private fun setOnClickListeners() {
        binding.apply {
            ivLeftRock.apply {
                setOnClickListener {
                    setBackgroundResource(R.drawable.result_background)
                    setImageResource(R.drawable.rock_left_pressed)
                    rpsGameManager.movePlayerOne("ROCK")
                    rpsGameManager.startGame()
                }
            }

            ivLeftPaper.apply {
                setOnClickListener {
                    setBackgroundResource(R.drawable.result_background)
                    setImageResource(R.drawable.paper_left_pressed)
                    rpsGameManager.movePlayerOne("PAPER")
                    rpsGameManager.startGame()
                }
            }

            ivLeftScissors.apply {
                setOnClickListener {
                    setBackgroundResource(R.drawable.result_background)
                    setImageResource(R.drawable.scissor_left_pressed)
                    rpsGameManager.movePlayerOne("SCISSORS")
                    rpsGameManager.startGame()
                }
            }

            ivRefresh.setOnClickListener {
                rpsGameManager.resetGame()
            }
        }
    }

    override fun onPlayerStatusChanged(player: Player) {
        setCharacterType(player)
    }

    override fun onResetImage() {
        binding.apply {
            ivLeftRock.apply {
                setBackgroundResource(android.R.color.transparent)
                setImageResource(R.drawable.rock_left_idle)
            }
            ivLeftPaper.apply {
                setBackgroundResource(android.R.color.transparent)
                setImageResource(R.drawable.paper_left_idle)
            }
            ivLeftScissors.apply {
                setBackgroundResource(android.R.color.transparent)
                setImageResource(R.drawable.scissor_left_idle)
            }
            ivRightRock.apply {
                setBackgroundResource(android.R.color.transparent)
                setImageResource(R.drawable.rock_right_idle)
            }
            ivRightPaper.apply {
                setBackgroundResource(android.R.color.transparent)
                setImageResource(R.drawable.paper_right_idle)
            }
            ivRightScissors.apply {
                setBackgroundResource(android.R.color.transparent)
                setImageResource(R.drawable.scissor_right_idle)
            }
        }
    }

    override fun onGameStateChanged(gameState: GameState) {
        binding.apply {
            tvStatus.text = ""
            ivRefresh.visibility = when (gameState) {
                GameState.IDLE -> View.INVISIBLE
                GameState.STARTED -> View.INVISIBLE
                GameState.FINISHED -> View.VISIBLE
            }
        }
    }

    override fun onGameFinished(gameState: GameState, winner: Player) {
        when (winner.playerSide) {
            PlayerSide.DRAW -> {
                binding.tvStatus.text = getString(R.string.draw)
            }
            PlayerSide.PLAYER_ONE -> {
                binding.tvStatus.text = getString(R.string.you_win)
            }
            else -> {
                binding.tvStatus.text = getString(R.string.you_lose)
            }
        }
    }

    private fun setCharacterType(player: Player) {
        val ivCharTop: ImageView?
        val ivCharMiddle: ImageView?
        val ivCharBottom: ImageView?

        ivCharTop = binding.ivRightRock
        ivCharMiddle = binding.ivRightPaper
        ivCharBottom = binding.ivRightScissors

        if (player.playerState == PlayerState.CHOOSED) {
            when (player.playerType) {
                PlayerType.ROCK -> {
                    ivCharTop.apply {
                        setBackgroundResource(R.drawable.result_background)
                        setImageResource(R.drawable.rock_right_pressed)
                    }
                }
                PlayerType.PAPER -> {
                    ivCharMiddle.apply {
                        setBackgroundResource(R.drawable.result_background)
                        setImageResource(R.drawable.paper_right_pressed)
                    }
                }
                PlayerType.SCISSORS -> {
                    ivCharBottom.apply {
                        setBackgroundResource(R.drawable.result_background)
                        setImageResource(R.drawable.scissor_right_pressed)
                    }
                }
            }
        }
    }
}