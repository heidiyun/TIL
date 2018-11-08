package kr.ac.ajou.heidi.beatbox

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import java.io.IOException

class BeatBox(context: Context) {
    companion object {
        const val TAG = "BeatBox"
        const val SOUNDS_FOLDER = "sample_sounds"
    }

    private val assets: AssetManager = context.assets
    val sounds = arrayListOf<Sound>()

    init {
        loadSounds()
    }


    private fun loadSounds() {
        val soundNames : Array<String>

        try {
            soundNames = assets.list(SOUNDS_FOLDER)!!
            Log.i(TAG, "Found ${soundNames.size} sounds")
        } catch (e : IOException) {
            Log.e(TAG, "Could not list assets", e)
            return
        }

        for(filename in soundNames) {
            val assetPath = "$SOUNDS_FOLDER/$filename"
            Log.i(TAG, "assetPath $assetPath")
            val sound  = Sound(assetPath)
            sounds.add(sound)
        }
    }


}