package com.chydee.lacasadepapel.welcome

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class WelcomeUserViewModel : ViewModel() {
    private var db: FirebaseFirestore = Firebase.firestore

    var _playerName = String()
    val playerName: String
        get() = _playerName


    var _playerScore = String()
    val playerScore: String
        get() = _playerScore


    fun getPlayerData(playerId: String): Task<DocumentSnapshot> {
        val docRef = db.collection("players").document(playerId)
        return docRef.get()
    }

    override fun onCleared() {
        super.onCleared()
        onCleared()
    }


}
