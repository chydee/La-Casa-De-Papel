package com.chydee.lacasadepapel.welcome

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class WelcomeUserViewModel : ViewModel() {
    private var db: FirebaseFirestore = Firebase.firestore

    private var _playerName = MutableLiveData<String?>()
    val playerName: LiveData<String?>
        get() = _playerName

    private var _playerScore = MutableLiveData<String?>()
    val playerScore: LiveData<String?>
        get() = _playerScore

    fun getPlayerData(playerId: String) {
        val docRef = db.collection("players").document(playerId)
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    Log.d("Welcome", "DocumentSnapshot data: ${documentSnapshot.data}")
                    _playerName = documentSnapshot.data?.get("name") as MutableLiveData<String?>
                    val score = documentSnapshot.data?.get("score")
                    _playerScore = score as MutableLiveData<String?>
                } else {
                    Log.d("Welcome", "No such document")
                }
            }

            .addOnFailureListener { exception ->
                Log.d("Welcome", "get failed with ", exception)
            }
    }

}
