package com.chydee.lacasadepapel.game

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.chydee.lacasadepapel.R

class GameResult : Fragment() {

    companion object {
        fun newInstance() = GameResult()
    }

    private lateinit var viewModel: GameResultViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.game_result_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GameResultViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
