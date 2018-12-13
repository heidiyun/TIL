package kr.ac.ajou.heidi.photogallery

import android.content.Context
import android.preference.PreferenceManager

class QueryPreferences {
    companion object {
        const val PREF_SEARCH_QUERY = "searchQuery"
        const val PREF_LAST_RESULT_ID = "lastResultId"
        const val PREF_IS_ALARM_ON = "isAlarmOn"

        fun getStoredQuery(context: Context): String? =
                PreferenceManager.getDefaultSharedPreferences(context)
                    .getString(PREF_SEARCH_QUERY, null)

        fun setStoredQuery(context: Context, query: String?) =
                PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putString(PREF_SEARCH_QUERY, query)
                    .apply()

        fun getLastResultId(context: Context) = PreferenceManager.getDefaultSharedPreferences(context)
            .getString(PREF_LAST_RESULT_ID, null)

        fun setLastResultId(context: Context, lastResultId: String) {
            PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(PREF_LAST_RESULT_ID, lastResultId)
                .apply()
        }

        fun isAlarmOn(context: Context): Boolean {
            return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_IS_ALARM_ON, false)
        }

        fun setAlarmOn(context: Context, isOn: Boolean) {
            PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_IS_ALARM_ON, isOn)
                .apply()
        }
    }
}