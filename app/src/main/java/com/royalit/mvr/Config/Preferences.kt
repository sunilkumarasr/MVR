package com.royalit.mvr.Config

import android.content.Context
import android.content.SharedPreferences

object Preferences {

    private const val PREFERENCE_NAME = "MVR"

    const val LOGINCHECK = "LOGINCHECK"
    const val userId = "userId"
    const val referralId = "referralId"
    const val empId = "empId"
    const val name = "name"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    fun saveFloatValue(context: Context, key: String, value: Float) {
        with(getSharedPreferences(context).edit()) {
            putFloat(key, value)
            apply()
        }
    }

    fun loadFloatValue(context: Context, key: String, defValue: Float): Float {
        return getSharedPreferences(context).getFloat(key, defValue)
    }

    fun saveStringValue(context: Context, key: String, value: String) {
        with(getSharedPreferences(context).edit()) {
            putString(key, value)
            apply()
        }
    }

    fun loadStringValue(context: Context, key: String, defValue: String): String? {
        return getSharedPreferences(context).getString(key, defValue)
    }

    fun saveLongValue(context: Context, key: String, value: Long) {
        with(getSharedPreferences(context).edit()) {
            putLong(key, value)
            apply()
        }
    }

    fun loadLongValue(context: Context, key: String, defValue: Long): Long {
        return getSharedPreferences(context).getLong(key, defValue)
    }

    fun saveIntegerValue(context: Context, key: String, value: Int) {
        with(getSharedPreferences(context).edit()) {
            putInt(key, value)
            apply()
        }
    }

    fun loadIntegerValue(context: Context, key: String, defValue: Int): Int {
        return getSharedPreferences(context).getInt(key, defValue)
    }

    fun saveBooleanValue(context: Context, key: String, value: Boolean) {
        with(getSharedPreferences(context).edit()) {
            putBoolean(key, value)
            apply()
        }
    }

    fun deleteSharedPreferences(context: Context) {
        with(getSharedPreferences(context).edit()) {
            clear()
            commit() // Note: `commit()` is synchronous; you may want to use `apply()` for async behavior
        }
    }

}