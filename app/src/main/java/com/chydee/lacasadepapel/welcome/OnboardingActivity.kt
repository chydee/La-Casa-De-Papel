package com.chydee.lacasadepapel.welcome

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chydee.lacasadepapel.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityOnboardingBinding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
