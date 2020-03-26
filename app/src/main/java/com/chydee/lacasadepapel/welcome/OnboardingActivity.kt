package com.chydee.lacasadepapel.welcome

import android.content.Context
import android.os.Bundle
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

        // Toast.makeText(applicationContext, id, Toast.LENGTH_LONG).show()
        if (id?.isEmpty()!! || id == "") {
            navController.navigate(R.id.action_welcomeUserFragment_to_onBoardFragment)
        } else {
            navController.navigate(R.id.action_onBoardFragment_to_welcomeUserFragment)
        }
    }
}
