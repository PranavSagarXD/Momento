package com.sagar.momento.update

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import com.sagar.momento.BuildConfig

private val GITHUB_API_URL =
    "https://api.github.com/repos/PranavSagarXD/Momento/releases/latest"

private val json = Json { ignoreUnknownKeys = true }

private fun isNewerVersion(latest: String, current: String): Boolean {
    val latestParts = latest.split(".").mapNotNull { it.toIntOrNull() }
    val currentParts = current.split(".").mapNotNull { it.toIntOrNull() }
    val maxLen = maxOf(latestParts.size, currentParts.size)
    for (i in 0 until maxLen) {
        val l = latestParts.getOrElse(i) { 0 }
        val c = currentParts.getOrElse(i) { 0 }
        if (l != c) return l > c
    }
    return false
}

data class UpdateInfo(
    val latestVersion: String,
    val downloadUrl: String,
    val releaseNotes: String,
)

class UpdateChecker {

    suspend fun check(): UpdateInfo? = withContext(Dispatchers.IO) {
        try {
            val response = fetchUrl(GITHUB_API_URL)
            val root = json.parseToJsonElement(response).jsonObject

            val tagName = root["tag_name"]?.jsonPrimitive?.content ?: return@withContext null
            val latestVersion = tagName.removePrefix("v")
            val currentVersion = BuildConfig.VERSION_NAME

            if (!isNewerVersion(latestVersion, currentVersion)) return@withContext null

            val body = root["body"]?.jsonPrimitive?.content ?: ""
            val assets = root["assets"]?.jsonArray ?: return@withContext null

            val apkAsset = assets.firstOrNull { asset ->
                val name = asset.jsonObject["name"]?.jsonPrimitive?.content ?: ""
                name.endsWith(".apk") && name.contains("Momento")
            } ?: return@withContext null

            val downloadUrl = apkAsset.jsonObject["browser_download_url"]?.jsonPrimitive?.content
                ?: return@withContext null

            UpdateInfo(
                latestVersion = tagName,
                downloadUrl = downloadUrl,
                releaseNotes = body.take(500),
            )
        } catch (e: Exception) {
            Log.w("UpdateChecker", "Failed to check for updates", e)
            null
        }
    }

    private fun fetchUrl(urlString: String): String {
        val conn = URL(urlString).openConnection() as HttpURLConnection
        conn.setRequestProperty("Accept", "application/vnd.github.v3+json")
        conn.setRequestProperty("User-Agent", "Momento-Android")
        conn.connectTimeout = 10000
        conn.readTimeout = 10000
        return try {
            BufferedReader(InputStreamReader(conn.inputStream)).readText()
        } finally {
            conn.disconnect()
        }
    }
}
