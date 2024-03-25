package com.example.quizgame.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.quizgame.R
import com.example.quizgame.databinding.ActivityQuestionBinding
import com.example.quizgame.model.Question
import com.example.quizgame.model.Score
import com.example.quizgame.viewmodel.QuestionViewModel

class QuestionActivity : AppCompatActivity() {

    private lateinit var mQuestionBinding : ActivityQuestionBinding
    private lateinit var viewModel : QuestionViewModel
    private lateinit var answer : String

    private var countdownTimer : CountDownTimer? = null
    private var correctAnswerNum : Int = 0
    private var incorrectAnswerNum : Int = 0
    private var isAnswerSelected: Boolean = false
    private var isIncreased : Boolean = false // đánh dấu đã tăng câu sai


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        viewModel = QuestionViewModel()

        mQuestionBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_question
        )
        mQuestionBinding.viewModel = viewModel

        mQuestionBinding.finishBtn.setOnClickListener {

            val score = Score(viewModel.getQuestionNum()!! - correctAnswerNum, correctAnswerNum)
            val bundle = Bundle().apply {
                putParcelable("scoreKey", score)

            }
            //show dialog and move to result page
            showQuizGameDialog(this, bundle)
        }

        mQuestionBinding.nextQueBtnx.setOnClickListener{
            if (!isAnswerSelected && !isIncreased){
                // Người dùng không chọn đáp án, tăng số câu sai lên 1
                incorrectAnswerNum++
                mQuestionBinding.numIncorrectAnswer.text = incorrectAnswerNum.toString()
                viewModel.setIncorrectAnswerNum(incorrectAnswerNum)
            }

            val _currentIndexQues = viewModel.getCurrentIndexQuestion()
            if (_currentIndexQues < viewModel.getQuestionNum()!!-1){
                viewModel.setCurrentIndexQuestion(_currentIndexQues + 1)
                resetUI()
                loadQuestions(viewModel.getCurrentIndexQuestion())
            }
            else{
                val bundle = bundleToResult(incorrectAnswerNum, correctAnswerNum)
                showQuizGameDialog(this, bundle)
            }
        }
        loadQuestions(viewModel.getCurrentIndexQuestion())
        viewModel.getQuestion()
    }

    fun bundleToResult(incorrectAnswerNum: Int, correctAnswerNum: Int): Bundle{
        val score = Score(incorrectAnswerNum, correctAnswerNum)
        val bundle = Bundle().apply {
            putParcelable("scoreKey", score)
        }
        return bundle
    }

    fun showQuizGameDialog(context: Context, bundle: Bundle){
        val score = Score(incorrectAnswerNum, correctAnswerNum)
        val alertDialogBuilder = AlertDialog.Builder(context) // xay dung dialog
        alertDialogBuilder.setTitle("Quiz Game")
        alertDialogBuilder.setMessage("Congratulations!!! \n You have answered all the questions. Do you want to see the result?")

        // "Play again" button
        alertDialogBuilder.setPositiveButton("Play again"){ _, _ ->
            //reset
            incorrectAnswerNum = 0
            correctAnswerNum = 0
            viewModel.setIncorrectAnswerNum(incorrectAnswerNum)
            viewModel.setCorrectAnswerNum(correctAnswerNum)
            val intent = Intent(this, QuestionActivity::class.java)
            startActivity(intent)
        }
        // "See result"
        alertDialogBuilder.setNegativeButton("See result") { _, _ ->
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("scoreKey", score)
            startActivity(intent)
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun resetUI(){

        mQuestionBinding.option1Btnx.setBackgroundColor(android.graphics.Color.WHITE)
        mQuestionBinding.option2Btnx.setBackgroundColor(android.graphics.Color.WHITE)
        mQuestionBinding.option3Btnx.setBackgroundColor(android.graphics.Color.WHITE)
        mQuestionBinding.option4Btnx.setBackgroundColor(android.graphics.Color.WHITE)

        mQuestionBinding.option1Btnx.isEnabled = true
        mQuestionBinding.option2Btnx.isEnabled = true
        mQuestionBinding.option3Btnx.isEnabled = true
        mQuestionBinding.option4Btnx.isEnabled = true

        countdownTimer?.cancel()
        mQuestionBinding.tvTimerCount.text = ""

    }

    private fun loadQuestions(currentIndexQuestion : Int){
        hideQuizViews()
        viewModel.questionMutableLiveData.observe(this){
            listQuestion ->
            if (listQuestion != null){
                showQuizViews()
                displayQuizData(listQuestion[currentIndexQuestion], currentIndexQuestion)
                viewModel.setCurrentIndexQuestion(currentIndexQuestion)
            }
        }
    }

    private fun hideQuizViews(){
        mQuestionBinding.option1Btnx.visibility = View.INVISIBLE
        mQuestionBinding.option3Btnx.visibility = View.INVISIBLE
        mQuestionBinding.option2Btnx.visibility = View.INVISIBLE
        mQuestionBinding.option4Btnx.visibility = View.INVISIBLE
        mQuestionBinding.questionContent.visibility = View.INVISIBLE
        mQuestionBinding.nextQueBtnx.visibility = View.INVISIBLE
        mQuestionBinding.finishBtn.visibility = View.INVISIBLE
        mQuestionBinding.textView4.visibility = View.INVISIBLE
        mQuestionBinding.textView2.visibility = View.INVISIBLE
        mQuestionBinding.numCorrectAnswer.visibility = View.INVISIBLE
        mQuestionBinding.numIncorrectAnswer.visibility = View.INVISIBLE
        mQuestionBinding.textViewx.visibility = View.INVISIBLE
        mQuestionBinding.timeCountText.visibility = View.INVISIBLE
        mQuestionBinding.tvTimerCount.visibility = View.INVISIBLE
        mQuestionBinding.questionNumber.visibility = View.INVISIBLE

    }

    private fun showQuizViews(){
        mQuestionBinding.pbProgressBar.visibility = View.GONE
        mQuestionBinding.option1Btnx.visibility = View.VISIBLE
        mQuestionBinding.option3Btnx.visibility = View.VISIBLE
        mQuestionBinding.option2Btnx.visibility = View.VISIBLE
        mQuestionBinding.option4Btnx.visibility = View.VISIBLE
        mQuestionBinding.questionContent.visibility = View.VISIBLE
        mQuestionBinding.nextQueBtnx.visibility = View.VISIBLE
        mQuestionBinding.finishBtn.visibility = View.VISIBLE
        mQuestionBinding.textView4.visibility = View.VISIBLE
        mQuestionBinding.textView2.visibility = View.VISIBLE
        mQuestionBinding.numCorrectAnswer.visibility = View.VISIBLE
        mQuestionBinding.numIncorrectAnswer.visibility = View.VISIBLE
        mQuestionBinding.textViewx.visibility = View.VISIBLE
        mQuestionBinding.timeCountText.visibility = View.VISIBLE
        mQuestionBinding.tvTimerCount.visibility = View.VISIBLE
        mQuestionBinding.questionNumber.visibility = View.VISIBLE
    }

    fun displayQuizData(questionModel: Question, questionOrder: Int){
        isAnswerSelected = false
        isIncreased = false

        countdownTimer?.cancel()

        mQuestionBinding.questionContent.text = questionModel.questionContent
        mQuestionBinding.option1Btnx.text = questionModel.option_a
        mQuestionBinding.option2Btnx.text = questionModel.option_b
        mQuestionBinding.option3Btnx.text = questionModel.option_c
        mQuestionBinding.option4Btnx.text = questionModel.option_d
        answer = questionModel.answer
        mQuestionBinding.questionNumber.text = (questionOrder+1).toString()

        mQuestionBinding.option1Btnx.setOnClickListener {
            checkAnswer(questionModel.option_a.trim(), questionModel.answer.trim())
            disableButton()
        }
        mQuestionBinding.option2Btnx.setOnClickListener {
            checkAnswer(questionModel.option_b.trim(), questionModel.answer.trim())
            disableButton()
        }
        mQuestionBinding.option3Btnx.setOnClickListener {
            checkAnswer(questionModel.option_c.trim(), questionModel.answer.trim())
            disableButton()
        }
        mQuestionBinding.option4Btnx.setOnClickListener {
            checkAnswer(questionModel.option_d.trim(), questionModel.answer.trim())
            disableButton()
        }
        countTimer()
    }

    fun checkAnswer(selectedAns : String, answer: String){
        when (answer){
            mQuestionBinding.option1Btnx.text.toString() -> setButtonBackground(mQuestionBinding.option1Btnx, true)
            mQuestionBinding.option2Btnx.text.toString() -> setButtonBackground(mQuestionBinding.option2Btnx, true)
            mQuestionBinding.option3Btnx.text.toString() -> setButtonBackground(mQuestionBinding.option3Btnx, true)
            mQuestionBinding.option4Btnx.text.toString() -> setButtonBackground(mQuestionBinding.option4Btnx, true)
        }
        if (selectedAns == answer){
            correctAnswerNum++
            mQuestionBinding.numCorrectAnswer.text = correctAnswerNum.toString()
            viewModel.setCorrectAnswerNum(correctAnswerNum)
        } else {
            incorrectAnswerNum += 1
            isIncreased = true
            mQuestionBinding.numIncorrectAnswer.text = incorrectAnswerNum.toString()
            viewModel.setIncorrectAnswerNum(incorrectAnswerNum)

            when(selectedAns){
                mQuestionBinding.option1Btnx.text.toString() -> setButtonBackground(mQuestionBinding.option1Btnx, false)
                mQuestionBinding.option2Btnx.text.toString() -> setButtonBackground(mQuestionBinding.option2Btnx, false)
                mQuestionBinding.option3Btnx.text.toString() -> setButtonBackground(mQuestionBinding.option3Btnx, false)
                mQuestionBinding.option4Btnx.text.toString() -> setButtonBackground(mQuestionBinding.option4Btnx, false)
            }
        }
        // đánh dấu user đã chọn đáp án
        isAnswerSelected = true
    }

    private fun disableButton(){
        mQuestionBinding.option1Btnx.isEnabled = false
        mQuestionBinding.option2Btnx.isEnabled = false
        mQuestionBinding.option3Btnx.isEnabled = false
        mQuestionBinding.option4Btnx.isEnabled = false
    }

    fun countTimer(){
        countdownTimer = object : CountDownTimer(7000, 1000){
            override fun onTick(p0: Long) {
                // Thực hiện hành động trong mỗi tick (1 giây trong trường hợp này)
                val secondsRemaining = p0 / 1000
                mQuestionBinding.tvTimerCount.text = "$secondsRemaining"
            }

            override fun onFinish() {
                mQuestionBinding.tvTimerCount.text = "0"

                if(!isAnswerSelected){
                    // Nếu không chọn đáp án
                    incorrectAnswerNum++
                    isIncreased = true
                    mQuestionBinding.numIncorrectAnswer.text = incorrectAnswerNum.toString()
                    viewModel.setIncorrectAnswerNum(incorrectAnswerNum)
                }

                when (answer) {
                    mQuestionBinding.option1Btnx.text.toString() -> setButtonBackground(mQuestionBinding.option1Btnx, true)
                    mQuestionBinding.option2Btnx.text.toString() -> setButtonBackground(mQuestionBinding.option2Btnx, true)
                    mQuestionBinding.option3Btnx.text.toString() -> setButtonBackground(mQuestionBinding.option3Btnx, true)
                    mQuestionBinding.option4Btnx.text.toString() -> setButtonBackground(mQuestionBinding.option4Btnx, true)
                }

                mQuestionBinding.questionContent.text = "Sorry, Time is up! Continue \n with next question."
                disableButton()
            }
        }.start()
    }

    private fun setButtonBackground(button: Button, isCorrect: Boolean){
        if (isCorrect){
            button.setBackgroundColor(Color.GREEN)
        }
        else{
            button.setBackgroundColor(Color.RED)
        }
    }
}