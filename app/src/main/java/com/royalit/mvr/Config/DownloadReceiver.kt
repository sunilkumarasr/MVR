package com.royalit.mvr.Config

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class DownloadReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val downloadId = intent?.getLongExtra(android.app.DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        if (downloadId != -1L) {
            Toast.makeText(context, "Download completed!", Toast.LENGTH_SHORT).show()
        }
    }
}