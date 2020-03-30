package com.chydee.lacasadepapel.game

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ShareCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.chydee.lacasadepapel.R
import com.chydee.lacasadepapel.databinding.GameResultFragmentBinding


class GameResult : Fragment() {

    val args: GameResultArgs by navArgs()

    private lateinit var viewModel: GameResultViewModel
    private lateinit var binding: GameResultFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // findNavController().navigate(GameResultDirections.actionGameResultToWelcomeUserFragment())
                    findNavController().popBackStack(R.id.welcome_fragment, true)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        // The callback can be enabled or disabled here or in the lambda
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.game_result_fragment, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(GameResultViewModel::class.java)
        binding.scoreView.text = args.point.toString()
        if (null == getShareIntent()?.resolveActivity(activity!!.packageManager)) {
            binding.shareBtn.isVisible = false
        }
        binding.shareBtn.setOnClickListener { shareSuccess() }
        binding.playAgain.setOnClickListener {
            findNavController().navigate(GameResultDirections.actionGameResultToWelcomeUserFragment())
        }
        // The callback can be enabled or disabled here or in handleOnBackPressed()
    }

    private fun getShareIntent(): Intent? {
        return activity?.let {
            ShareCompat.IntentBuilder.from(it)
                .setText(getString(R.string.share_success_text, args.point)).setType("text/plain")
                .intent
        }
    }

    private fun shareSuccess() {
        startActivity(getShareIntent())
    }


}

