package com.chydee.lacasadepapel.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.chydee.lacasadepapel.R
import com.chydee.lacasadepapel.ui.base.BaseFragment

class LeaderBoardFragment : BaseFragment() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(LeaderBoardViewModel::class.java)
    }
}
