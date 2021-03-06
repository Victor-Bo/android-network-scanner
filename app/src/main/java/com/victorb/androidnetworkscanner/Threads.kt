package com.victorb.androidnetworkscanner

import android.os.Handler
import android.os.Looper

/**
 * Runs the callback on the UI thread
 */
fun runOnMainThread(callback: () -> Unit) {
    Handler(Looper.getMainLooper()).post {
        callback.invoke()
    }
}

/**
 * Runs the callback on the UI thread after delay miliseconds
 */
fun runOnMainThreadDelayed(delay: Long, callback: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({
        callback.invoke()
    }, delay)
}