package com.chydee.lacasadepapel.game

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.chydee.lacasadepapel.Quiz
import com.chydee.lacasadepapel.R
import com.chydee.lacasadepapel.databinding.GameFragmentBinding
import com.google.android.material.button.MaterialButton
import kotlinx.android.parcel.RawValue
import java.util.*

class GameFragment : Fragment() {
    private lateinit var viewModel: GameViewModel
    private lateinit var binding: GameFragmentBinding
    private lateinit var quizes: ArrayList<Quiz>

    private var currentQuestion: Quiz? = null
    private var questionId = 0
    private var isCancelled = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        quizes = ArrayList()
        viewModel.getQuiz().addOnSuccessListener { documents ->

            Log.d("QUIZES", "${documents.documents}")
            for (docs in documents) {
                quizes.add(
                    Quiz(
                        question = docs.get("question"),
                        options = docs.get("options") as @RawValue List<Any?>,
                        answer = docs.get("answer")
                    )
                )
            }
            currentQuestion = quizes[questionId]
            setupQuizView()
            setupButtons()
        }
    }

    private fun setupQuizView() {
        binding.questionTextView.text = currentQuestion?.question as CharSequence?
        binding.btnFirstAnswer.text = currentQuestion?.options?.get(0) as CharSequence?
        binding.btnSecondAnswer.text = currentQuestion?.options?.get(1) as CharSequence?
        binding.btnThirdAnswer.text = currentQuestion?.options?.get(2) as CharSequence?
        binding.btnFourthAnswer.text = currentQuestion?.options?.get(3) as CharSequence?

        questionId++
    }

    private fun setupButtons() {
        binding.btnFirstAnswer.setOnClickListener {
            answerEvaluator(
                binding.btnFirstAnswer,
                binding.btnFirstAnswer.text.toString()
            )
        }
        binding.btnSecondAnswer.setOnClickListener {
            answerEvaluator(
                binding.btnSecondAnswer,
                binding.btnSecondAnswer.text.toString()
            )
        }
        binding.btnThirdAnswer.setOnClickListener {
            answerEvaluator(
                binding.btnThirdAnswer,
                binding.btnThirdAnswer.text.toString()
            )
        }

        binding.btnFourthAnswer.setOnClickListener {
            answerEvaluator(
                binding.btnFourthAnswer,
                binding.btnFourthAnswer.text.toString()
            )
        }
    }

    private fun answerEvaluator(button: MaterialButton, answer: String) {
        val isAnswer = checkAnswer(answer)
        if (isAnswer) button.setStrokeColorResource(R.color.green)
        else button.setStrokeColorResource(R.color.primaryColor)

        val handler = Handler()
        handler.postDelayed({
            button.setStrokeColorResource(R.color.secondaryTextColor)
            showNextQuiz()
        }, 300)
    }

    private fun checkAnswer(answer: String): Boolean {
        if (currentQuestion?.answer?.equals(answer)!!) {
            //TODO Increment Score, and set the score
            return true
        }
        return false
    }

    private fun showNextQuiz() {
        if (questionId < quizes.size) {
            currentQuestion = quizes[questionId]
            setupQuizView()
        } else {
            isCancelled = true
            //TODO To GameResult
        }
    }


}
