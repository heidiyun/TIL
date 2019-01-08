package kr.ac.ajou.heidi.criminalintentk.model

import java.text.SimpleDateFormat
import java.util.*

class Crime(uuid: UUID) {

    var id: UUID = uuid
    var date: Date = Date()
    var title: String = ""
    var solved: Boolean = false

    constructor() : this(UUID.randomUUID())



}


val dateFormat = SimpleDateFormat("EEEE, MMM d, YYYY", Locale.US)
