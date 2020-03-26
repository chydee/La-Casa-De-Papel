package com.chydee.lacasadepapel.welcome

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class WelcomeUserViewModel : ViewModel() {
    private var db: FirebaseFirestore = Firebase.firestore

    var _playerName = String()
    var _playerScore = String()


    fun getPlayerData(playerId: String): Task<DocumentSnapshot> {
        val docRef = db.collection("players").document(playerId)
        // Source can be CACHE, SERVER, or DEFAULT.
        val source = Source.CACHE
        return docRef.get(source)
    }

    override fun onCleared() {
        super.onCleared()
        onCleared()
    }


}
