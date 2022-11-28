package com.rivera.bot

import android.util.Log

object DtkLog {

    fun print(message: String) {
        val tag = "11111"
        Log.e(tag, message)
    }

    fun print(tag: String, message: String) {
        Log.e(tag, message)
    }

    fun print(any: Any?) {
        Log.e("11111", any.toString())
    }

    fun print(tag: String, any: Any) {
        Log.e(tag, any.toString())
    }
}