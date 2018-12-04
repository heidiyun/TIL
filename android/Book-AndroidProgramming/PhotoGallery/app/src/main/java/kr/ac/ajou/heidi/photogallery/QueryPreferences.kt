package kr.ac.ajou.heidi.photogallery

import android.content.Context
import android.preference.PreferenceManager

class QueryPreferences {
    companion object {
        const val PREF_SEARCH_QUERY = "searchQuery"

        fun getStoredQuery(context: Context): String? =
                PreferenceManager.getDefaultSharedPreferences(context)
                    .getString(PREF_SEARCH_QUERY, null)

        fun setStoredQuery(context: Context, query: String?) =
                PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putString(PREF_SEARCH_QUERY, query)
                    .apply()
    }
}