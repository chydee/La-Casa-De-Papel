package com.chydee.lacasadepapel.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.chydee.lacasadepapel.R
import com.chydee.lacasadepapel.databinding.OnBoardFragmentBinding

class OnBoardFragment : Fragment() {

    private lateinit var viewModel: OnBoardViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: OnBoardFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.on_board_fragment, container, false)
        viewModel = ViewModelProvider(this).get(OnBoardViewModel::class.java)

        binding.continueButton.setOnClickListener {
            findNavController().navigate(OnBoardFragmentDirections.actionOnBoardFragmentToWelcomeUserFragment())
        }
        return binding.root
    }

}
