package com.chydee.lacasadepapel.ui.welcome

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chydee.lacasadepapel.data.Player
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class WelcomeUserViewModel constructor(private val db: FirebaseFirestore) : ViewModel() {

    private val _name = MutableLiveData<String>()
    val name: LiveData<String>
        get() = _name

    private val _score = MutableLiveData<String>()
    val score: LiveData<String>
        get() = _score


    private var job = Job()
    private var scope = CoroutineScope(job + Dispatchers.Default)

    fun getPlayerData(playerId: String) {
        val docRef = db.collection("players").document(playerId)
        // Source can be CACHE, SERVER, or DEFAULT.
        val source = Source.CACHE
        docRef.get(source).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result?.toObject<Player>()
                Log.d("Welcome", "DocumentSnapshot data: $document")
                _name.postValue(document?.name!!) //documentSnapshot["name"].toString()
                val score = document.score
                _score.postValue(score!!.toString())
            }

            if (task.isCanceled) {
                Log.d("Welcome", "Cancelled")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}
