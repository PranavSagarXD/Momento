package com.sagar.momento.update

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppUpdater(private val context: Context) {

    suspend fun downloadAndInstall(
        downloadUrl: String,
        onProgress: (Float) -> Unit = {},
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val conn = URL(downloadUrl).openConnection() as HttpURLConnection
            conn.setRequestProperty("User-Agent", "Momento-Android")
            conn.connectTimeout = 15000
            conn.readTimeout = 30000
            conn.connect()

            val fileLength = conn.contentLengthLong
            val inputStream = conn.inputStream

            val apkFile = File(context.cacheDir, "Momento-update.apk")
            FileOutputStream(apkFile).use { outputStream ->
                val buffer = ByteArray(8192)
                var bytesRead: Int
                var totalRead = 0L
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                    totalRead += bytesRead
                    if (fileLength > 0) {
                        onProgress(totalRead.toFloat() / fileLength)
                    }
                }
            }

            inputStream.close()
            conn.disconnect()

            installApk(apkFile)
            true
        } catch (e: Exception) {
            android.util.Log.e("AppUpdater", "Failed to download/install update", e)
            false
        }
    }

    private fun installApk(apkFile: File) {
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            apkFile,
        )

        val intent = Intent(Intent.ACTION_INSTALL_PACKAGE).apply {
            data = uri
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(intent)
    }
}
