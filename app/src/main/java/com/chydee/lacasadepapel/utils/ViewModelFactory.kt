package com.chydee.lacasadepapel.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chydee.lacasadepapel.ui.OnBoardViewModel
import com.chydee.lacasadepapel.ui.game.GameViewModel
import com.chydee.lacasadepapel.ui.welcome.WelcomeUserViewModel
import com.google.firebase.firestore.FirebaseFirestore

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val db: FirebaseFirestore
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OnBoardViewModel::class.java)) {
            return OnBoardViewModel(db) as T
        }
        if (modelClass.isAssignableFrom(WelcomeUserViewModel::class.java)) {
            return WelcomeUserViewModel(db) as T
        }

        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(db) as T
        }

        throw IllegalArgumentException("Unknown class name")
    }
}
