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
package com.sagar.momento.core

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ColorSpace
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface

fun Uri.getBitmapWithRotation(context: Context): Bitmap? {
    val contentResolver = context.contentResolver
    val orientation =
        contentResolver.openFileDescriptor(this, "r")?.use { pfd ->
            ExifInterface(pfd.fileDescriptor)
                .getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
        } ?: ExifInterface.ORIENTATION_UNDEFINED

    val options =
        BitmapFactory.Options().apply {
            inPreferredConfig = Bitmap.Config.ARGB_8888
            inPreferredColorSpace = ColorSpace.get(ColorSpace.Named.SRGB)
        }
    val bitmap =
        contentResolver.openInputStream(this)?.use { stream ->
            BitmapFactory.decodeStream(stream, null, options)
        } ?: return null

    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
        else -> bitmap
    }
}

private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    if (rotatedBitmap != bitmap) {
        bitmap.recycle()
    }
    return rotatedBitmap
}
