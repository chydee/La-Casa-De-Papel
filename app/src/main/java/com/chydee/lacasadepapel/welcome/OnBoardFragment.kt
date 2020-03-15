package com.chydee.lacasadepapel.welcome

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.chydee.lacasadepapel.R

class OnBoardFragment : Fragment() {

    companion object {
        fun newInstance() = OnBoardFragment()
    }

    private lateinit var viewModel: OnBoardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.on_board_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OnBoardViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
