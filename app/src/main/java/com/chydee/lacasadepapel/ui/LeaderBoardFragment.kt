package com.chydee.lacasadepapel.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.chydee.lacasadepapel.R

class LeaderBoardFragment : Fragment() {

    companion object {
        fun newInstance() = LeaderBoardFragment()
    }

    private lateinit var viewModel: LeaderBoardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.leader_board_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LeaderBoardViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
