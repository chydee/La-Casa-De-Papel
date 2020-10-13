package com.chydee.lacasadepapel.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chydee.lacasadepapel.data.Player
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*

class OnBoardViewModel constructor(private val db: FirebaseFirestore) : ViewModel() {

    private val _player = MutableLiveData<DocumentReference>()
    val player: LiveData<DocumentReference>
        get() = _player

    private var job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)


    fun addPlayer(player: Player) {
        scope.launch {
            withContext(Dispatchers.Default) {
                val players = db.collection("players")
                players.add(player).addOnSuccessListener {
                    _player.postValue(it)
                }
                    .addOnFailureListener {
                        it.printStackTrace()
                        _player.postValue(null)
                    }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}

