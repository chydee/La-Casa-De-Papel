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

const val TAG = "OnBoardFragment"

class OnBoardFragment : Fragment() {

    private lateinit var viewModel: OnBoardViewModel
    private lateinit var binding: OnBoardFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.on_board_fragment, container, false)
        viewModel = ViewModelProvider(this).get(OnBoardViewModel::class.java)
        val player = hashMapOf(
            "name" to binding.editTextPlayerName.text.toString(),
            "score" to 0
        )
        binding.continueButton.setOnClickListener {
            viewModel.setUpDb(player)
            findNavController().navigate(OnBoardFragmentDirections.actionOnBoardFragmentToWelcomeUserFragment())
        }

        return binding.root
    }

}
