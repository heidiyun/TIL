package kr.ac.ajou.heidi.criminalintentk.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import kr.ac.ajou.heidi.criminalintentk.database.CrimeBaseHelper
import kr.ac.ajou.heidi.criminalintentk.database.CrimeCursorWrapper
import kr.ac.ajou.heidi.criminalintentk.database.CrimeDbSchema
import java.util.*

class CrimeLab private constructor(val context: Context) {

    var database: SQLiteDatabase = CrimeBaseHelper(context).writableDatabase
    val crimes: ArrayList<Crime> = arrayListOf()
        get() {
            val cursor = queryCrimes(null, null)

            cursor.use {c ->
                c.moveToFirst()
                while (!c.isAfterLast) {
                    c.getCrime()?.let {
                        crimes.add(it)
                    }
                    c.moveToNext()
                }
            }

            return field
        }

    companion object {
        var isCrime = false
        var crimeLab: CrimeLab? = null
        fun get(context: Context): CrimeLab? {
            if (!isCrime) {
                crimeLab = CrimeLab(context)
                isCrime = true

            }
            return crimeLab
        }

        fun getContentValues(crime: Crime): ContentValues {
            val values = ContentValues()
            values.put(CrimeDbSchema.CrimeTable.Cols.UUID, crime.id.toString())
            values.put(CrimeDbSchema.CrimeTable.Cols.TITLE, crime.title)
            values.put(CrimeDbSchema.CrimeTable.Cols.DATE, crime.date.time)
            values.put(CrimeDbSchema.CrimeTable.Cols.SOLVED, if (crime.solved) 1 else 0)

            return values
        }
    }


    fun getCrime(id: UUID): Crime? {
        val cursor = queryCrimes(
                CrimeDbSchema.CrimeTable.Cols.UUID + " = ?",
                arrayOf(id.toString())
        )

        cursor.use { cursor ->
            if (cursor.count == 0) {
                return null
            }

            cursor.moveToFirst()
            return cursor.getCrime()
        }
    }

    fun addCrime(c: Crime) {
        val values = getContentValues(c)
        database.insert(CrimeDbSchema.CrimeTable.NAME, null, values)
    }

    fun updateCrime(crime: Crime) {
        val uuidString = crime.id.toString()
        val values = getContentValues(crime)

        database.update(CrimeDbSchema.CrimeTable.NAME, values, CrimeDbSchema.CrimeTable.Cols.UUID + " = ?",
                arrayOf(uuidString))
    }

    private fun queryCrimes(whereClause: String?, whereArgs: Array<String>?): CrimeCursorWrapper {
        val cursor = database.query(
                CrimeDbSchema.CrimeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        )

        return CrimeCursorWrapper(cursor)
    }
}