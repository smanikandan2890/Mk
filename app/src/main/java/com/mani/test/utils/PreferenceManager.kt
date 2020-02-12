package com.mani.test.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(ctx: Context) {
    private val KEY_BASE_URL = "URL"

    var context = ctx
    private val prefName = "TEST"
    private var editor: SharedPreferences.Editor? = null

    var preferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

    fun saveBaseUrl(baseUrl: String) {
        editor = preferences.edit()
        editor?.putString(KEY_BASE_URL, baseUrl)
        editor?.apply()
    }

    fun getBaseURL(): String? {
        return preferences!!.getString(KEY_BASE_URL, "")
    }
}