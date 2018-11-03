package kr.ac.ajou.heidi.todolist

import android.content.Context
import android.preference.PreferenceManager

const val KEY_ACCESS_TOKEN = "kr.ac.ajou.heidi.todolist.access_token"

fun updateToken(context: Context, token: String) {
    PreferenceManager.getDefaultSharedPreferences(context).edit()
        .putString(KEY_ACCESS_TOKEN, token)
        .apply()
}

fun getToken(context: Context): String? {
    return PreferenceManager.getDefaultSharedPreferences(context)
        .getString(KEY_ACCESS_TOKEN, null)
}