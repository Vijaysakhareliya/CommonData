package com.basecontent.utils

import android.content.Context
import android.content.SharedPreferences
import com.basecontent.MyApp

object SharedPrefHelper {

    private const val SHARED_PREFS_NAME = "commonPreferences"

    private var sharedPreferences: SharedPreferences =
        MyApp.getInstance()
            .getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    fun save(key: String, value: Any?) {
        val editor = sharedPreferences.edit()
        when {
            value is Boolean -> editor.putBoolean(key, (value as Boolean?)!!)
            value is Int -> editor.putInt(key, (value as Int?)!!)
            value is Float -> editor.putFloat(key, (value as Float?)!!)
            value is Long -> editor.putLong(key, (value as Long?)!!)
            value is String -> editor.putString(key, value as String?)
            value is Enum<*> -> editor.putString(key, value.toString())
            value != null -> throw RuntimeException("Attempting to save non-supported preference")
        }
        editor.apply()
    }

    fun delete(key: String) {
        if (sharedPreferences.contains(key)) {
            getEditor().remove(key).commit()
        }
    }

    fun clearAll() {
        getEditor().clear().commit()
    }

    private fun getEditor(): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(key: String): T {
        return sharedPreferences.all[key] as T
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(key: String, defValue: T): T {
        val returnValue = sharedPreferences.all[key] as T
        return returnValue ?: defValue
    }
}