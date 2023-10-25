package com.example.supabasedemo

import android.content.Context
import android.content.SharedPreferences

class SharedPreference(val context: Context) {
    private val PREFS_NAME = "ServerURL"
    val sharedPref: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    fun SaveString(KEY_NAME: String, URL: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_NAME, URL)
        editor.commit()
    }
    fun SaveBool(KEY_NAME: String, Bool: Boolean) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean(KEY_NAME, Bool)
        editor.commit()
    }

    fun GetBool(KEY_NAME: String): Boolean {
        return sharedPref.getBoolean(KEY_NAME, false)
    }
    fun GetString(KEY_NAME: String): String? {
        return sharedPref.getString(KEY_NAME, "")
    }
}