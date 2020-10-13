package com.chydee.lacasadepapel.ui.welcome

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source

class WelcomeUserViewModel constructor(private val db: FirebaseFirestore) : ViewModel() {
    var _playerName = String()
    var _playerScore = String()


    fun getPlayerData(playerId: String): Task<DocumentSnapshot> {
        val docRef = db.collection("players").document(playerId)
        // Source can be CACHE, SERVER, or DEFAULT.
        val source = Source.CACHE
        return docRef.get(source)
    }

}
