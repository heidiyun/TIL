package kr.ac.ajou.heidi.beatbox

import android.content.Context
import android.content.res.AssetManager
import android.media.AudioManager
import android.media.SoundPool
import android.util.Log
import java.io.IOException

class BeatBox(context: Context) {
    companion object {
        const val TAG = "BeatBox"
        const val SOUNDS_FOLDER = "sample_sounds"
        const val MAX_SOUNDS = 5
    }

    private val assets: AssetManager = context.assets
    private val soundPool = SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0)
    val sounds = arrayListOf<Sound>()

    init {
        loadSounds()
    }


    private fun loadSounds() {
        val soundNames: Array<String>

        try {
            soundNames = assets.list(SOUNDS_FOLDER)!!
            Log.i(TAG, "Found ${soundNames.size} sounds")
        } catch (e: IOException) {
            Log.e(TAG, "Could not list assets", e)
            return
        }

        for (filename in soundNames) {
            try {
                val assetPath = "$SOUNDS_FOLDER/$filename"
                val sound = Sound(assetPath)
                load(sound)
                sounds.add(sound)
            } catch (e: IOException) {
                Log.e(TAG, "Could not load sound $filename", e)
            }
        }
    }


    @Throws(IOException::class)
    private fun load(sound: Sound) {
        val assetFileDescriptor = assets.openFd(sound.assetPath)
        val soundId = soundPool.load(assetFileDescriptor, 1)
        sound.soundId = soundId
    }

    fun play(sound: Sound) {
        sound.soundId?.let {
            soundPool
                .play(it, 1.0f, 1.0f, 1, 0, 1.0f)
        } ?: return

    }

    fun release() {
        soundPool.release()
    }


}