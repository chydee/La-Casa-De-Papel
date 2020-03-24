package com.chydee.lacasadepapel.welcome

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.chydee.lacasadepapel.R
import com.chydee.lacasadepapel.databinding.WelcomeUserFragmentBinding

class WelcomeUserFragment : Fragment() {

    companion object {
        fun newInstance() = WelcomeUserFragment()
    }

    private lateinit var viewModel: WelcomeUserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: WelcomeUserFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.welcome_user_fragment, container, false)

        val mediaPlayer: MediaPlayer? = MediaPlayer.create(context, R.raw.intro)
        mediaPlayer?.start() // no need to call prepare(); create() does that for you

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(WelcomeUserViewModel::class.java)
        // TODO: Use the ViewModel
    }


}
