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

    }

    fun getCrime(id: UUID): Crime? {
        for (i in 0..crimes.size) {
            if (crimes[i].id == id) {
                return crimes[i]
            }
        }
        return null
    }

    fun addCrime(c: Crime) {
        crimes.add(c)
    }


}