/*
 * Copyright (C) 2026  PranavSagarXD
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.sagar.momento.data

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.toAndroidUri
import java.io.File
import kotlin.time.Clock
import org.koin.core.annotation.Single
import com.sagar.momento.core.data_classes.Day

@Single
class ImageHandler(private val context: Context) {
    fun copyImageToAppData(day: Day): Uri {
        val destDirectory = File(context.filesDir, day.projectId.toString())
        if (!destDirectory.exists()) destDirectory.mkdirs()

        val destFile = File(destDirectory, "${day.date}_${Clock.System.now().epochSeconds}")

        destDirectory.listFiles()?.find { it.name.startsWith("${day.date}_") }?.delete()

        val uri = PlatformFile(day.image).toAndroidUri()
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            destFile.outputStream().use { outputStream -> inputStream.copyTo(outputStream) }
        }

        return destFile.toUri()
    }

    fun deleteDayImage(day: Day) {
        try {
            val uri = PlatformFile(day.image).toAndroidUri()
            uri.toFile().delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
