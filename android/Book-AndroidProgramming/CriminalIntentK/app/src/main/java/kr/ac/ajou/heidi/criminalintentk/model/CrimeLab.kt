package kr.ac.ajou.heidi.criminalintentk.model

import java.util.*

class CrimeLab private constructor() {

    val crimes: MutableList<Crime> = mutableListOf()

    companion object {
        lateinit var crimeLab: CrimeLab
        var isCrime = false
        fun get(): CrimeLab {
            if (!isCrime) {
                crimeLab = CrimeLab()
                isCrime = true
            }
            return crimeLab
        }
    }

    init {
        for (i in 0..100) {
            val crime = Crime()
            crime.title = "범죄 # $i"
            crime.solved = i % 2 == 0
            crimes.add(crime)
        }
    }

    fun getCrime(id: UUID): Crime? {
        for (i in 0..crimes.size) {
            if (crimes[i].id == id) {
                return crimes[i]
            }
        }
        return null
    }


}