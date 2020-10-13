package com.chydee.lacasadepapel.ui.welcome

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.chydee.lacasadepapel.R
import com.chydee.lacasadepapel.data.Player
import com.chydee.lacasadepapel.databinding.WelcomeUserFragmentBinding
import com.chydee.lacasadepapel.ui.ViewModelFactory
import com.chydee.lacasadepapel.ui.base.BaseFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class WelcomeUserFragment : BaseFragment() {

    private lateinit var viewModel: WelcomeUserViewModel
    private lateinit var binding: WelcomeUserFragmentBinding

    private lateinit var vmFactory: ViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.welcome_user_fragment, container, false)
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vmFactory = ViewModelFactory(Firebase.firestore)
        viewModel = ViewModelProvider(this, vmFactory).get(WelcomeUserViewModel::class.java)
        requestFocus("https://res.cloudinary.com/dvscfg5kr/video/upload/v1600469852/LA_CASA_DE_PAPEL_OPENING_SONG_HQ_SOUNDTRACK.mp3")
        val get = viewModel.getPlayerData(getPlayerId()!!)
        get.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result?.toObject<Player>()
                Log.d("Welcome", "DocumentSnapshot data: $document")
                viewModel._playerName = document?.name!!//documentSnapshot["name"].toString()
                val score = document.score
                viewModel._playerScore = score!!.toString()
                binding.playerNameTextView.text = viewModel._playerName
                binding.playerCurrentScore.text = viewModel._playerScore
            } else {
                Toast.makeText(context, "Profile Creation Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

        binding.startGameBtn.setOnClickListener {
            findNavController().navigate(WelcomeUserFragmentDirections.actionWelcomeUserFragmentToGameFragment())
        }

    }

    private fun getPlayerId(): String? {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        return sharedPref!!.getString(getString(R.string.id_key), "")
    }

}
