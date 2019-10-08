package com.gnoemes.shikimori.utils.widgets

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.media.session.MediaButtonReceiver

class MockMediaButtonReceiver : MediaButtonReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            super.onReceive(context, intent)
        } catch (e: IllegalStateException) {
            Log.d(this.javaClass.name, e.message)
        }

    }
}