package com.chydee.lacasadepapel.ui.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class GameViewModel constructor(private val db: FirebaseFirestore) : ViewModel() {

    private val _quizes = MutableLiveData<QuerySnapshot>()
    val quizes: LiveData<QuerySnapshot>
        get() = _quizes

    private var job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Default)

    fun getQuiz() {
        scope.launch {
            val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
            db.firestoreSettings = settings
            db.collection("quizes").get().addOnSuccessListener {
                _quizes.postValue(it)
            }
                .addOnFailureListener { _quizes.postValue(null) }
        }
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

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
