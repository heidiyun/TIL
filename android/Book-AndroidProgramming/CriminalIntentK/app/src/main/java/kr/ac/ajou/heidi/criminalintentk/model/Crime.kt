package kr.ac.ajou.heidi.criminalintentk.model

import java.text.SimpleDateFormat
import java.util.*

class Crime {

    val id: UUID = UUID.randomUUID()
    val date: Date = Date()
    lateinit var title: String
    var solved: Boolean = false
}

val dateFormat = SimpleDateFormat("EEEE, MMM d, YYYY", Locale.US)
