package com.chydee.lacasadepapel.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.chydee.lacasadepapel.databinding.WelcomeUserFragmentBinding
import com.chydee.lacasadepapel.ui.ViewModelFactory
import com.chydee.lacasadepapel.ui.base.BaseFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class WelcomeUserFragment : BaseFragment() {

    private lateinit var viewModel: WelcomeUserViewModel
    private lateinit var binding: WelcomeUserFragmentBinding

    private lateinit var vmFactory: ViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = WelcomeUserFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vmFactory = ViewModelFactory(Firebase.firestore)
        viewModel = ViewModelProvider(this, vmFactory).get(WelcomeUserViewModel::class.java)
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this
        binding.welcomeViewModel = viewModel
        requestFocus("https://res.cloudinary.com/dvscfg5kr/video/upload/v1600469852/LA_CASA_DE_PAPEL_OPENING_SONG_HQ_SOUNDTRACK.mp3")
        viewModel.getPlayerData(getPlayerId()!!)

        binding.startGameBtn.setOnClickListener {
            findNavController().navigate(WelcomeUserFragmentDirections.actionWelcomeUserFragmentToGameFragment())
        }

    }

}
