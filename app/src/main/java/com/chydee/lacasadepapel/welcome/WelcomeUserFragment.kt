package com.chydee.lacasadepapel.welcome

import android.content.Context
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.chydee.lacasadepapel.R
import com.chydee.lacasadepapel.databinding.WelcomeUserFragmentBinding


@Suppress("DEPRECATION")
class WelcomeUserFragment : Fragment() {

    private lateinit var viewModel: WelcomeUserViewModel
    private var mediaPlayer: MediaPlayer? = null
    private var audioManager: AudioManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: WelcomeUserFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.welcome_user_fragment, container, false)

        viewModel = ViewModelProvider(this).get(WelcomeUserViewModel::class.java)

        // Create and setup the {@link AudioManager} to request audio focus
        audioManager = activity!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        requestFocus()
        viewModel.getPlayerData(getPlayerId()!!)

        return binding.root
    }

    private fun getPlayerId(): String? {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val defaultValue = resources.getString(R.string.defaultId)
        val id = sharedPref!!.getString(getString(R.string.id_key), defaultValue)

        return id
    }

    private fun requestFocus() {
        // Request audio focus so in order to play the audio file. The app needs to play a
        // short audio file, so we will request audio focus with a short amount of time
        // with AUDIOFOCUS_GAIN_TRANSIENT.
        val result: Int = audioManager!!.requestAudioFocus(
            mOnAudioFocusChangeListener,
            AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
        )

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // We have audio focus now.

            // Create and setup the {@link MediaPlayer} for the audio resource associated
            // with the current word
            mediaPlayer = MediaPlayer.create(context, R.raw.intro)
            mediaPlayer!!.start() // no need to call prepare(); create() does that for you

            // Setup a listener on the media player, so that we can stop and release the
            // media player once the sound has finished playing.
            mediaPlayer!!.setOnCompletionListener(mCompletionListener)
        }
    }

    /**
     * This listener gets triggered whenever the audio focus changes
     * (i.e., we gain or lose audio focus because of another app or device).
     */
    private val mOnAudioFocusChangeListener =
        OnAudioFocusChangeListener { focusChange ->
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK
            ) {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                mediaPlayer!!.pause()
                mediaPlayer!!.seekTo(0)
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                mediaPlayer!!.start()
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                releaseMediaPlayer()
            }
        }

    /**
     * Clean up the media player by releasing its resources.
     */
    private fun releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mediaPlayer!!.release()

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mediaPlayer = null

            // Regardless of whether or not we were granted audio focus, abandon it. This also
            // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                audioManager!!.abandonAudioFocus(mOnAudioFocusChangeListener)
            }
        }
    }

    /**
     * This listener gets triggered when the [MediaPlayer] has completed
     * playing the audio file.
     */
    private val mCompletionListener =
        OnCompletionListener { // Now that the sound file has finished playing, release the media player resources.
            releaseMediaPlayer()
        }

    override fun onPause() {
        super.onPause()
        mediaPlayer!!.pause()
    }

    override fun onStop() {
        super.onStop()
        releaseMediaPlayer()
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer!!.start()
    }


}
