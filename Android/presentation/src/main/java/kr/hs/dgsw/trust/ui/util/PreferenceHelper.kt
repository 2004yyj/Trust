package kr.hs.dgsw.trust.ui.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

object PreferenceHelper {
    private lateinit var sharedPreferences: SharedPreferences
    var username: String
        get() {
            return sharedPreferences.getString("username", "")!!
        }
        set(value) {
            val edit = sharedPreferences.edit()
            edit.putString("username", value)
            edit.apply()
        }

    var token: String?
        get() {
            return sharedPreferences.getString("token", "")
        }
        set(value) {
            val edit = sharedPreferences.edit()
            edit.putString("token", value)
            edit.apply()
        }

    var autoLogin: Boolean
        get() {
            return sharedPreferences.getBoolean("autoLogin", false)
        }
        set(value) {
            val edit = sharedPreferences.edit()
            edit.putBoolean("autoLogin", value)
            edit.apply()
        }

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("user", MODE_PRIVATE)
    }



    fun clear() {
        val edit = sharedPreferences.edit()
        edit.putString("token", "")
        edit.putString("username", "")
        edit.putBoolean("autoLogin", false)
        edit.apply()
    }
}