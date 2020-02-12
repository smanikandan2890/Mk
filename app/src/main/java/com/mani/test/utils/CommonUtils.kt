package com.mani.test.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.widget.Toast
import java.util.*
import java.util.regex.Pattern

class CommonUtils {
    companion object {
        fun getHeaders(ctx: Context): Map<String, String> {
            val headers = HashMap<String, String>()

            headers["Authorization"] = ""
            headers["logintype"] = "1"
            headers["deviceid"] = getDeviceId(ctx)
            headers["devicetype"] = "android"
            headers["appVersion"] = "1.3.1"

            return headers
        }

        @SuppressLint("HardwareIds")
        fun getDeviceId(context: Context): String {
            return Settings.Secure.getString(context.applicationContext.contentResolver, Settings.Secure.ANDROID_ID)
        }

        fun showMessage(context: Context, message: String?) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        val EMAIL_PATTERN = Pattern.compile("^[-a-z0-9~!$%^&*_=+}{\\'?]+(\\.[-a-z0-9~!$%^&*_=+}{\\'?]+)*@([a-z0-9_][-a-z0-9_]*(\\.[-a-z0-9_]+)*\\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,5})?$")
    }
}