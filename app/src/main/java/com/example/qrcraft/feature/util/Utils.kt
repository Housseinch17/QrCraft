package com.example.qrcraft.feature.util

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SEND

fun Context.shareText(text: String) {
    val sendIntent: Intent = Intent().apply {
        action = ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    this.startActivity(shareIntent)
}