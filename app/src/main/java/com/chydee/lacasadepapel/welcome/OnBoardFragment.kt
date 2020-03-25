package com.chydee.lacasadepapel.welcome

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.chydee.lacasadepapel.Player
import com.chydee.lacasadepapel.R
import com.chydee.lacasadepapel.databinding.OnBoardFragmentBinding

const val TAG = "OnBoardFragment"

class OnBoardFragment : Fragment() {

    private lateinit var viewModel: OnBoardViewModel
    private lateinit var binding: OnBoardFragmentBinding
    private var playerName: Editable? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.on_board_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(OnBoardViewModel::class.java)
        playerName = binding.editTextPlayerName.text
        binding.continueButton.setOnClickListener {
            if (binding.editTextPlayerName.text.isEmpty() || binding.editTextPlayerName.text.toString() == "") {
                binding.editTextPlayerName.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.wineColor))
                binding.editTextPlayerName.setError(
                    "Required",
                    resources.getDrawable(R.drawable.ic_error)
                )
            } else {
                addPlayer()
            }

        }
    }

    private fun savePlayerId(id: String) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(getString(R.string.id_key), id)
            apply()
        }
    }

    private fun addPlayer() {
        viewModel.addPlayer(Player(name = playerName.toString(), score = 0))
            .addOnSuccessListener { documentReference ->
                savePlayerId(documentReference.id)
                Log.d(TAG, "Player ${documentReference.id} has been added")
                findNavController().navigate(OnBoardFragmentDirections.actionOnBoardFragmentToWelcomeUserFragment())
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Unable to add Player : ${exception.message}")
            }
    }

}

