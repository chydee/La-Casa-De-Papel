package com.chydee.lacasadepapel.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.chydee.lacasadepapel.R
import com.chydee.lacasadepapel.data.Player
import com.chydee.lacasadepapel.databinding.OnBoardFragmentBinding
import com.chydee.lacasadepapel.ui.base.BaseFragment

const val TAG = "OnBoardFragment"

class OnBoardFragment : BaseFragment() {

    private var interval: Long = 100
    private var count: Int = 5

    private lateinit var viewModel: OnBoardViewModel
    private lateinit var binding: OnBoardFragmentBinding
    private var playerName: Editable? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = OnBoardFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(OnBoardViewModel::class.java)
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this
        playerName = binding.editTextPlayerName.text
        binding.continueButton.setOnClickListener {
            if (binding.editTextPlayerName.text.isEmpty() || binding.editTextPlayerName.text.toString() == "") {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    binding.editTextPlayerName.backgroundTintList =
                        ColorStateList.valueOf(resources.getColor(R.color.primaryColor, null))
                }
                binding.editTextPlayerName.setError(
                    "Required",
                    resources.getDrawable(R.drawable.ic_error, null)
                )
            } else {
                showProgressBar()
            }
        }
    }

    private fun addPlayer() {
        viewModel.addPlayer(
            Player(
                name = playerName.toString(),
                score = 0
            )
        )
        viewModel.player.observe(viewLifecycleOwner, {
            if (it != null) {
                savePlayerId(it.id)
                Log.d(TAG, "Player ${it.id} has been added")
                findNavController().navigate(OnBoardFragmentDirections.actionOnBoardFragmentToWelcomeUserFragment())
            } else {
                Log.d(TAG, "Unable to add Player")
            }
        })
    }

    private fun showProgressBar() {
        val progressBar = binding.progressBar
        binding.continueButton.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
        var progress = 0
        while (progress < 105) {
            Thread.sleep(interval)
            progressBar.progress = progress
            progress += count
        }
        addPlayer()
    }


}

