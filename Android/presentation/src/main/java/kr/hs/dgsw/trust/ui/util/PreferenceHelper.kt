package kr.hs.dgsw.trust.ui.util

import android.content.Context
import android.content.Context.MODE_PRIVATE

object PreferenceHelper {
    private const val USERNAME = "USER_USERNAME"
    private const val PASSWORD = "USER_PASSWORD"

    fun init(context: Context, username: String, password: String) {
        val sharedPreferences = context.getSharedPreferences(USERNAME, MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        edit.putString(USERNAME, username)
        edit.putString(PASSWORD, password)
        edit.apply()
    }

    fun setUsername(context: Context, username: String) {
        val sharedPreferences = context.getSharedPreferences(USERNAME, MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        edit.putString(USERNAME, username)
        edit.apply()
    }

    fun getUsername(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(USERNAME, MODE_PRIVATE)
        return sharedPreferences.getString(USERNAME, "")
    }

    fun setPassword(context: Context, password: String) {
        val sharedPreferences = context.getSharedPreferences(USERNAME, MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        edit.putString(PASSWORD, password)
        edit.apply()
    }

    fun getPassword(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(USERNAME, MODE_PRIVATE)
        return sharedPreferences.getString(PASSWORD, "")
    }
}