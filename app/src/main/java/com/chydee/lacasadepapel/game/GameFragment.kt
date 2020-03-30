package com.chydee.lacasadepapel.game

import android.animation.Animator
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.chydee.lacasadepapel.R
import com.chydee.lacasadepapel.databinding.GameFragmentBinding
import com.chydee.lacasadepapel.models.Quiz
import com.google.android.material.button.MaterialButton
import kotlinx.android.parcel.RawValue
import java.util.*


class GameFragment : Fragment() {
    private lateinit var viewModel: GameViewModel
    private lateinit var binding: GameFragmentBinding
    private lateinit var quizes: ArrayList<Quiz>

    private var currentQuestion: Quiz? = null
    private var questionId = 0
    private var score = 0
    private var scoreIncrement = 5


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // findNavController().navigate(GameResultDirections.actionGameResultToWelcomeUserFragment())
                    findNavController().popBackStack(R.id.welcome_fragment, false)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        // The callback can be enabled or disabled here or in the lambda
    }

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
            for (docs in documents) {
                quizes.add(
                    Quiz(
                        question = docs.get("question"),
                        options = docs.get("options") as @RawValue List<Any?>,
                        answer = docs.get("answer")
                    )
                )
            }
            try {
                currentQuestion = quizes[questionId]
                setupQuizView()
                setupButtons()
            } catch (e: IndexOutOfBoundsException) {
                Log.d("QUESTIONS", "IndexOutOfBound ${quizes.size}")
            }

        }
        setUpTimerAnimation()
        binding.quitQuizButton.setOnClickListener {
            findNavController().popBackStack(R.id.welcome_fragment, false)
        }
    }

    private fun setUpTimerAnimation() {
        binding.timeBomb.progress = 0F
        binding.timeBomb.pauseAnimation()
        binding.timeBomb.playAnimation()
        binding.timeBomb.addAnimatorListener(object :
            Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                Log.e("Animation:", "start")
            }

            override fun onAnimationEnd(animation: Animator) {
                Log.e("Animation:", "end")
                //Your code for remove the fragment
                val points = binding.scoreView.text.toString().toInt()
                getPlayerId()?.let { viewModel.updatePlayerScore(it, points) }
                findNavController().navigate(
                    GameFragmentDirections.actionGameFragmentToGameResult(
                        point = points
                    )
                )

            }

            override fun onAnimationCancel(animation: Animator) {
                Log.e("Animation:", "cancel")
            }

            override fun onAnimationRepeat(animation: Animator) {
                Log.e("Animation:", "repeat")
            }
        })
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
            score += scoreIncrement
            binding.scoreView.text = "$score"
            return true
        }
        return false
    }

    private fun showNextQuiz() {
        if (questionId < quizes.size) {
            currentQuestion = quizes[questionId]
            setupQuizView()
        } else {
            val points = binding.scoreView.text.toString().toInt()
            getPlayerId()?.let { viewModel.updatePlayerScore(it, points) }
            findNavController().navigate(
                GameFragmentDirections.actionGameFragmentToGameResult(
                    point = points
                ),
                NavOptions.Builder()
                    .setPopUpTo(R.id.welcome_fragment, false).build()
            )
        }
    }

    private fun getPlayerId(): String? {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        return sharedPref!!.getString(getString(R.string.id_key), "")
    }

}
