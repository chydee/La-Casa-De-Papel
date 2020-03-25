package com.chydee.lacasadepapel.welcome

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class OnBoardViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    val db = Firebase.firestore


    fun setUpDb(player: Any) {
        db.collection("players")
            .add(player)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding Document", e)
            }
    }


}

