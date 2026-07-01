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
package com.sagar.momento.montage

import android.media.MediaFormat
import java.io.File

enum class SlideAnimation {
    NONE,
    FADE_IN,
    SCALE_IN,
    SLIDE_LEFT,
    SLIDE_RIGHT,
}

data class MuxerConfiguration(
    val file: File,
    val videoWidth: Int = 1080,
    val videoHeight: Int = 1920,
    val mimeType: String = MediaFormat.MIMETYPE_VIDEO_AVC,
    val framesPerImage: Int = 1,
    val framesPerSecond: Float = 30f,
    val bitrate: Int = 5_000_000,
    val iFrameInterval: Int = 1,
    val animation: SlideAnimation = SlideAnimation.SLIDE_LEFT,
)

fun MuxerConfiguration.createFrameMuxer(): FrameMuxer =
    Mp4FrameMuxer(file.absolutePath, framesPerSecond)

interface MuxingCompletionListener {
    fun onVideoSuccessful(file: File)

    fun onVideoError(error: Throwable)
}

sealed interface MuxingResult {
    data class MuxingSuccess(val file: File) : MuxingResult

    data class MuxingError(val message: String, val exception: Exception) : MuxingResult
}
