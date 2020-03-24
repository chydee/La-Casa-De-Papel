package com.chydee.lacasadepapel.welcome

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.chydee.lacasadepapel.R
import com.chydee.lacasadepapel.databinding.OnBoardFragmentBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

const val TAG = "OnBoardFragment"

class OnBoardFragment : Fragment() {

    private lateinit var viewModel: OnBoardViewModel
    private var db: FirebaseFirestore? = null
    private lateinit var binding: OnBoardFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.on_board_fragment, container, false)
        viewModel = ViewModelProvider(this).get(OnBoardViewModel::class.java)
        setupDB()
        binding.continueButton.setOnClickListener {
            findNavController().navigate(OnBoardFragmentDirections.actionOnBoardFragmentToWelcomeUserFragment())
        }

        return binding.root
    }

    private fun setupDB() {
        // Access a Cloud Firestore instance from your Activity
        db = Firebase.firestore
        val player = hashMapOf(
            "name" to binding.editTextPlayerName.text.toString(),
            "score" to 0
        )

        db!!.collection("players")
            .add(player)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding Document", e)
            }
    }

}
