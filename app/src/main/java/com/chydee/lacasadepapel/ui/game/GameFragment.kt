package com.chydee.lacasadepapel.ui.game

import android.animation.Animator
import android.content.Context
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.chydee.lacasadepapel.R
import com.chydee.lacasadepapel.data.Quiz
import com.chydee.lacasadepapel.databinding.GameFragmentBinding
import com.chydee.lacasadepapel.ui.ViewModelFactory
import com.chydee.lacasadepapel.ui.base.BaseFragment
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.parcel.RawValue
import kotlinx.android.synthetic.main.game_fragment.*
import java.util.*


class GameFragment : BaseFragment() {
    private lateinit var viewModel: GameViewModel
    private lateinit var vmFactory: ViewModelFactory

    private lateinit var binding: GameFragmentBinding
    private lateinit var quizes: ArrayList<Quiz>

    private lateinit var currentQuestion: Quiz
    private var questionId = 0
    private var score = 0
    private var scoreIncrement = 5

    private lateinit var vibrator: Vibrator

    private lateinit var lottieView: LottieAnimationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vmFactory = ViewModelFactory(Firebase.firestore)
        viewModel = ViewModelProvider(this, vmFactory).get(GameViewModel::class.java)
        quizes = ArrayList()
        vibrator = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        binding.lifecycleOwner = this
        resetOptions()
        viewModel.getQuiz()
        viewModel.quizes.observe(viewLifecycleOwner, {
            if (it != null) {
                Toast.makeText(context, it.documents.size.toString(), Toast.LENGTH_LONG).show()
                for (docs in it.documents) {
                    quizes.add(
                        Quiz(
                            question = docs.get("question"),
                            options = docs.get("options") as @RawValue List<Any?>,
                            answer = docs.get("answer")
                        )
                    )
                    try {
                        currentQuestion = quizes[questionId]
                        setupQuizView()
                        lottieView.playAnimation()
                        setupButtons()
                    } catch (e: IndexOutOfBoundsException) {
                        Log.d("QUESTIONS", "IndexOutOfBound ${quizes.size}")
                    }
                }
            }
        })
        setUpTimerAnimation()
        handleClicks()
    }

    private fun handleClicks() {
        binding.quitQuizButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.nextButton.setOnClickListener {
            showNextQuiz()
        }
    }

    private fun setUpTimerAnimation() {
        lottieView = binding.timeBomb
        lottieView.progress = 0F
        lottieView.pauseAnimation()
        lottieView.addAnimatorListener(object :
            Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                Log.e("Animation:", "started")
            }

            override fun onAnimationEnd(animation: Animator) {
                Log.e("Animation:", "ended")
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
        binding.questionTextView.text = currentQuestion.question as CharSequence
        binding.btnFirstAnswer.text = currentQuestion.options[0] as CharSequence
        binding.btnSecondAnswer.text = currentQuestion.options[1] as CharSequence
        binding.btnThirdAnswer.text = currentQuestion.options[2] as CharSequence
        binding.btnFourthAnswer.text = currentQuestion.options[3] as CharSequence
    }

    private fun setupButtons() {
        binding.btnFirstAnswer.setOnClickListener {
            btn_second_answer.isEnabled = false
            btn_third_answer.isEnabled = false
            btn_fourth_answer.isEnabled = false
            answerEvaluator(
                binding.btnFirstAnswer,
                binding.btnFirstAnswer.text.toString()
            )
        }
        binding.btnSecondAnswer.setOnClickListener {
            btn_first_answer.isEnabled = false
            btn_third_answer.isEnabled = false
            btn_fourth_answer.isEnabled = false
            answerEvaluator(
                binding.btnSecondAnswer,
                binding.btnSecondAnswer.text.toString()
            )
        }
        binding.btnThirdAnswer.setOnClickListener {
            btn_first_answer.isEnabled = false
            btn_second_answer.isEnabled = false
            btn_fourth_answer.isEnabled = false
            answerEvaluator(
                binding.btnThirdAnswer,
                binding.btnThirdAnswer.text.toString()
            )
        }

        binding.btnFourthAnswer.setOnClickListener {
            btn_first_answer.isEnabled = false
            btn_second_answer.isEnabled = false
            btn_third_answer.isEnabled = false
            answerEvaluator(
                binding.btnFourthAnswer,
                binding.btnFourthAnswer.text.toString()
            )
        }
    }

    private fun answerEvaluator(button: MaterialButton, answer: String) {
        val isAnswer = checkAnswer(answer)
        if (isAnswer) {
            button.setStrokeColorResource(R.color.green)
        } else {
            button.setStrokeColorResource(R.color.primaryColor)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        200,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            }
        }
    }

    private fun checkAnswer(answer: String): Boolean {
        if (currentQuestion.answer?.equals(answer)!!) {
            score += scoreIncrement
            binding.scoreView.text = "$score"
            return true
        }
        return false
    }

    private fun showNextQuiz() {
        questionId++
        if (questionId < quizes.size) {
            currentQuestion = quizes[questionId]
            setupQuizView()
            resetOptions()
        } else {
            val points = binding.scoreView.text.toString().toInt()
            getPlayerId()?.let { viewModel.updatePlayerScore(it, points) }
            findNavController().navigate(
                GameFragmentDirections.actionGameFragmentToGameResult(
                    point = points
                )
            )
        }
    }

    private fun resetOptions() {
        with(binding) {
            btnFirstAnswer.apply {
                isEnabled = true
                setStrokeColorResource(R.color.secondaryTextColor)
            }
            btnSecondAnswer.apply {
                isEnabled = true
                setStrokeColorResource(R.color.secondaryTextColor)
            }
            btnThirdAnswer.apply {
                isEnabled = true
                setStrokeColorResource(R.color.secondaryTextColor)
            }
            btnFourthAnswer.apply {
                isEnabled = true
                setStrokeColorResource(R.color.secondaryTextColor)
            }
        }
    }

}
