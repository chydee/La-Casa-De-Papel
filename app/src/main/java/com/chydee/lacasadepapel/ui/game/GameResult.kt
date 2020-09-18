package com.chydee.lacasadepapel.ui.game

import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ShareCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.chydee.lacasadepapel.R
import com.chydee.lacasadepapel.databinding.GameResultFragmentBinding


class GameResult : Fragment() {

    val args: GameResultArgs by navArgs()

    private lateinit var binding: GameResultFragmentBinding

    private var mediaPlayer: MediaPlayer? = null
    private var audioManager: AudioManager? = null

    /**
     * This listener gets triggered whenever the audio focus changes
     * (i.e., we gain or lose audio focus because of another app or device).
     */
    private val mOnAudioFocusChangeListener =
        AudioManager.OnAudioFocusChangeListener { focusChange ->
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
     * This listener gets triggered when the [MediaPlayer] has completed
     * playing the audio file.
     */
    private val mCompletionListener =
        MediaPlayer.OnCompletionListener { // Now that the sound file has finished playing, release the media player resources.
            releaseMediaPlayer()
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
            mediaPlayer = MediaPlayer()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mediaPlayer!!.setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                        .build()
                )
            } else {
                mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            }
            try {
                mediaPlayer!!.setOnPreparedListener(OnPreparedListener { mp -> mp.start() })
                mediaPlayer!!.setDataSource("https://res.cloudinary.com/dvscfg5kr/video/upload/v1600469644/La_casa_de_papel_-_Bella_Ciao.mp3")
                mediaPlayer!!.prepareAsync()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // Setup a listener on the media player, so that we can stop and release the
            // media player once the sound has finished playing.
            mediaPlayer!!.setOnCompletionListener(mCompletionListener)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // findNavController().navigate(GameResultDirections.actionGameResultToWelcomeUserFragment())
                    findNavController().popBackStack(R.id.welcome_fragment, true)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        // The callback can be enabled or disabled here or in the lambda
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.game_result_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Create and setup the {@link AudioManager} to request audio focus
        audioManager = requireActivity().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        requestFocus()
        binding.scoreView.text = args.point.toString()
        if (null == getShareIntent()?.resolveActivity(requireActivity().packageManager)) {
            binding.shareBtn.isVisible = false
        }
        binding.shareBtn.setOnClickListener { shareSuccess() }
        binding.playAgain.setOnClickListener {
            findNavController().navigate(GameResultDirections.actionGameResultToWelcomeUserFragment())
        }
        // The callback can be enabled or disabled here or in handleOnBackPressed()
    }

    override fun onStop() {
        super.onStop()
        releaseMediaPlayer()
    }

    private fun getShareIntent(): Intent? {
        return activity?.let {
            ShareCompat.IntentBuilder.from(it)
                .setText(getString(R.string.share_success_text, args.point)).setType("text/plain")
                .intent
        }
    }

    private fun shareSuccess() {
        startActivity(getShareIntent())
    }


}

