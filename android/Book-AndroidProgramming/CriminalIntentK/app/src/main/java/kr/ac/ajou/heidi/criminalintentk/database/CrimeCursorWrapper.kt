package kr.ac.ajou.heidi.criminalintentk.database

import android.database.Cursor
import android.database.CursorWrapper
import kr.ac.ajou.heidi.criminalintentk.model.Crime
import java.util.*

class CrimeCursorWrapper(cursor: Cursor): CursorWrapper(cursor) {

    fun getCrime(): Crime? {
        val uuidString = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.UUID))
        val title = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.TITLE))
        val date = getLong(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.DATE))
        val isSolved = getInt(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.SOLVED))

        val crime = Crime(UUID.fromString(uuidString))
        crime.title = title
        crime.date = Date(date)
        crime.solved = isSolved != 0

        return crime
    }
}