package com.chydee.lacasadepapel.welcome

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.chydee.lacasadepapel.R
import com.chydee.lacasadepapel.databinding.ActivityOnboardingBinding


class OnboardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityOnboardingBinding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPref = this@OnboardingActivity.getPreferences(Context.MODE_PRIVATE)
        val id = sharedPref!!.getString(getString(R.string.id_key), "")
        val navController =
            Navigation.findNavController(this@OnboardingActivity, R.id.on_boarding_nav_host)

        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.onboarding_nav_graph)
        // Toast.makeText(applicationContext, id, Toast.LENGTH_LONG).show()
        if (id?.isEmpty()!! || id == "") {
            //graph.startDestination = R.id.on_boarding_fragment
        } else {
            //graph.startDestination = R.id.welcome_fragment
            Toast.makeText(applicationContext, id, Toast.LENGTH_LONG).show()
        }
    }
}
