package com.chydee.lacasadepapel.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        binding.playAgain.setOnClickListener {
            findNavController().navigate(GameResultDirections.actionGameResultToWelcomeUserFragment())
        }
    }

}
