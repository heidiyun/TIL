package kr.ac.ajou.heidi.criminalintentk.database

class CrimeDbSchema {
    object CrimeTable {
        val NAME = "crimes"

        object Cols {
            val UUID = "uuid"
            val TITLE = "title"
            val DATE = "date"
            val SOLVED = "solved"
            val SUSPECT = "suspect"
        }
    }
}


