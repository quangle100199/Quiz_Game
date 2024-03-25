package com.example.quizgame.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.quizgame.R
import com.example.quizgame.databinding.ActivityResultBinding
import com.example.quizgame.databinding.ActivitySignUpBinding
import com.example.quizgame.model.Score
import com.example.quizgame.viewmodel.ScoreViewModel

class ResultActivity : AppCompatActivity() {
    private lateinit var mResultBinding : ActivityResultBinding
    private lateinit var viewModel : ScoreViewModel

    private var correctAnswerNum : Int = 0
    private var incorrectAnswerNum : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_result)

        viewModel = ViewModelProvider(this)[ScoreViewModel::class.java]

        mResultBinding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(mResultBinding.root)

        setSupportActionBar(mResultBinding.toolbar)

        getBundle()
        mResultBinding.idNumCorrecAnswer.text = correctAnswerNum.toString()
        mResultBinding.idNumIncorrecAnswer.text = incorrectAnswerNum.toString()

        // gửi data lên firebase
        viewModel.sendScore(Score(incorrectAnswerNum, correctAnswerNum))

        // xử lý sự kiện
        mResultBinding.btnPlayAgain.setOnClickListener {
            startActivity(Intent(this, QuestionActivity::class.java))
        }

        mResultBinding.btnExit.setOnClickListener {
            startActivity(Intent(this, HomePageActivity::class.java))

        }
    }

    private fun getBundle() {
        val resultBundle = intent.extras
        if (resultBundle != null) {
            val result = resultBundle.getParcelable<Score>("scoreKey")

            if (result != null) {
                incorrectAnswerNum = result.incorrectNum
                correctAnswerNum = result.correctNum

                // Use incorrectNum and correctNum here
                Log.d("ScoreActivity", "Incorrect Num: $incorrectAnswerNum, Correct Num: $correctAnswerNum")
            }
        }
    }
}