package com.chydee.lacasadepapel.welcome

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class WelcomeUserViewModel : ViewModel() {
    private var db: FirebaseFirestore = Firebase.firestore


    fun getPlayerData(playerId: String) {
        val docRef = db.collection("players").document(playerId)
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    Log.d("Welcome", "DocumentSnapshot data: ${documentSnapshot.data}")
                } else {
                    Log.d("Welcome", "No such document")
                }
            }

            .addOnFailureListener { exception ->
                Log.d("Welcome", "get failed with ", exception)
            }
    }

}
