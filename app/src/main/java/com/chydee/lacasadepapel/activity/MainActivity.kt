package com.chydee.lacasadepapel.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ActivityNavigator
import androidx.navigation.Navigation
import com.chydee.lacasadepapel.R
import com.chydee.lacasadepapel.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPref = this@MainActivity.getPreferences(Context.MODE_PRIVATE)
        val id = sharedPref!!.getString(getString(R.string.id_key), "")
        val navController =
            Navigation.findNavController(this@MainActivity, R.id.on_boarding_nav_host)
        if (!id?.isEmpty()!!) {
            navController.navigate(R.id.action_onBoardFragment_to_welcomeUserFragment)
            return
        }
    }

    override fun finish() {
        super.finish()
        ActivityNavigator.applyPopAnimationsToPendingTransition(this)
    }
}
