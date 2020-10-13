package com.chydee.lacasadepapel.ui.game

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.chydee.lacasadepapel.R
import com.chydee.lacasadepapel.databinding.GameResultFragmentBinding
import com.chydee.lacasadepapel.ui.base.BaseFragment


class GameResult : BaseFragment() {

    val args: GameResultArgs by navArgs()

    private lateinit var binding: GameResultFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.game_result_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestFocus("https://res.cloudinary.com/dvscfg5kr/video/upload/v1600469644/La_casa_de_papel_-_Bella_Ciao.mp3")
        binding.scoreView.text = args.point.toString()
        if (null == getShareIntent()?.resolveActivity(requireActivity().packageManager)) {
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

