package kr.ac.ajou.heidi.beatbox

class Sound(val assetPath: String) {
    val name: String
    var soundId: Int? = null
    init {
        val components = assetPath.split("/")
        val filename = components.last()
        name = filename.replace(".wav", "")
    }


}