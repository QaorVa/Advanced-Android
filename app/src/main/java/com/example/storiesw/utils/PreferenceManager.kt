package com.example.storiesw.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.storiesw.utils.Constants.IS_LOGGED_IN
import com.example.storiesw.utils.Constants.KEY_TOKEN
import com.example.storiesw.utils.Constants.PREFS_NAME

class PreferenceManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_TOKEN, token)
        editor.apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(KEY_TOKEN, null)
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false)
    }

    fun clearPreferences() {
        val editor = sharedPreferences.edit()
        editor.remove(KEY_TOKEN)
        editor.remove(IS_LOGGED_IN)
        Log.d("PreferenceManager", "Preferences Cleared $KEY_TOKEN, $IS_LOGGED_IN")
        editor.apply()
    }

    companion object {
        @Volatile
        private var INSTANCE: PreferenceManager? = null

        fun getInstance(context: Context): PreferenceManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PreferenceManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}