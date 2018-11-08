package kr.ac.ajou.heidi.beatbox

class Sound(private val assetPath: String) {
    val name: String
    init {
        val components = assetPath.split("/")
        val filename = components.last()
        name = filename.replace(".wav", "")
    }


}