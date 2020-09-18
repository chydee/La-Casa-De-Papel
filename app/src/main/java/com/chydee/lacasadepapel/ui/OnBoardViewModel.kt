package com.chydee.lacasadepapel.ui

import androidx.lifecycle.ViewModel
import com.chydee.lacasadepapel.ui.models.Player
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class OnBoardViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    val db = Firebase.firestore

    fun addPlayer(player: Player): Task<DocumentReference> {
        val players = db.collection("players")
        return players.add(player)
    }


}

