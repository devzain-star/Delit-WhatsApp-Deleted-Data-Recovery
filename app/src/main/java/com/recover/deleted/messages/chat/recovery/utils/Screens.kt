package com.recover.deleted.messages.chat.recovery.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import java.io.ByteArrayOutputStream

class Screens(private var context: Context) {

    fun showClearTopScreen(cls: Class<*>?) {
        val intent = Intent(context, cls)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
    }

    fun showCustomScreen(cls: Class<*>?) {
        val intent = Intent(context, cls)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun showCustomScreenWithData(cls: Class<*>?, bundle: Bundle?) {
        val intent = Intent(context, cls)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtras(bundle!!)
        context.startActivity(intent)
    }

    fun showClearTopScreenWithData(cls: Class<*>?, bundle: Bundle?) {
        val intent = Intent(context, cls)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.putExtras(bundle!!)
        context.startActivity(intent)
    }
    fun shareIntent(value: String?) {
        val sendIntent = Intent()
        sendIntent.setAction(Intent.ACTION_SEND)
        sendIntent.putExtra(Intent.EXTRA_TEXT, value)
        sendIntent.setType("text/plain")
        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    fun copy(value: String?) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied", value)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show()
    }

    fun compressedBitmap(bitmap: Bitmap): ByteArray {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos) // 30 is the quality percentage
        return baos.toByteArray()
    }

    fun showToast(toast: String?) {
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show()
    }
}