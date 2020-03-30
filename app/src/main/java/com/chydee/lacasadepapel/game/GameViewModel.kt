package com.chydee.lacasadepapel.game

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class GameViewModel : ViewModel() {

    private var db: FirebaseFirestore = Firebase.firestore

    fun getQuiz(): Task<QuerySnapshot> {
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.firestoreSettings = settings
        val quizDocRef = db.collection("quizes")
        return quizDocRef.get()
    }

    fun updatePlayerScore(id: String, newScore: Int) {
        val docRef = db.collection("players").document(id)
        docRef.update("score", newScore)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(
                        "PlayerScore",
                        "Player $id's Score has been updated. newScore = $newScore\n${task.result}"
                    )
                } else {
                    Log.d(
                        "PlayerScore",
                        "Player $id's Score was updated. newScore = $newScore"
                    )
                }
            }
    }
}
