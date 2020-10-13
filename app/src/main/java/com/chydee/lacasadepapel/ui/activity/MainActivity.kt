package com.chydee.lacasadepapel.ui.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ActivityNavigator
import androidx.navigation.Navigation
import com.chydee.lacasadepapel.R
import com.chydee.lacasadepapel.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        ActivityNavigator.applyPopAnimationsToPendingTransition(this)
    }

    override fun onStart() {
        super.onStart()
        val intentFilterNW = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(connectivityReceiver, intentFilterNW)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(connectivityReceiver)
    }

    private val connectivityReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val cm = p0?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                cm.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        runOnUiThread {
                            kotlin.run {
                                // binding.connectivityStatus.text = getString(R.string.you_re_online) // Connection available
                            }
                        }
                    }

                    override fun onLost(network: Network) {
                        runOnUiThread {
                            kotlin.run {
                                // binding.connectivityStatus.text = getString(R.string.you_re_offline) // Connection lost
                            }
                        }
                    }
                }
                )
            } else {
                val nwInfo = cm.activeNetworkInfo
                val isConnected = nwInfo != null && nwInfo.isConnectedOrConnecting
                if (isConnected) {
                    // binding.connectivityStatus.text = getString(R.string.you_re_online)
                } else {
                    binding.connectivityStatus.text = getString(R.string.you_re_offline)
                }
            }

        }
    }
}
