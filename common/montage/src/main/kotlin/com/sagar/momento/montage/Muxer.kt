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

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.RawRes
import java.io.File
import java.io.IOException
import kotlinx.coroutines.flow.Flow

internal class Muxer(private val context: Context, private val file: File) {

    private var muxerConfig: MuxerConfiguration = MuxerConfiguration(file)
    private var muxingCompletionListener: MuxingCompletionListener? = null

    fun setMuxerConfig(config: MuxerConfiguration) {
        this.muxerConfig = config
    }

    fun getMuxerConfig() = muxerConfig

    fun setOnMuxingCompletedListener(listener: MuxingCompletionListener) {
        this.muxingCompletionListener = listener
    }

    suspend fun mux(imageFlow: Flow<Bitmap>, @RawRes audioTrack: Int? = null): MuxingResult {
        val frameBuilder = FrameBuilder(context, muxerConfig, audioTrack)
        try {
            frameBuilder.start()
        } catch (e: IOException) {
            muxingCompletionListener?.onVideoError(e)
            return MuxingResult.MuxingError("Start encoder failed", e)
        }

        try {
            imageFlow.collect { image -> frameBuilder.createFrame(image) }

            frameBuilder.releaseVideoCodec()

            if (audioTrack != null) frameBuilder.muxAudioFrames()

            frameBuilder.releaseAudioExtractor()
            frameBuilder.releaseMuxer()

            muxingCompletionListener?.onVideoSuccessful(file)
            return MuxingResult.MuxingSuccess(file)
        } catch (e: Exception) {
            try {
                frameBuilder.releaseVideoCodec()
            } catch (_: Exception) {}
            try {
                frameBuilder.releaseAudioExtractor()
            } catch (_: Exception) {}
            try {
                frameBuilder.releaseMuxer()
            } catch (_: Exception) {}

            muxingCompletionListener?.onVideoError(e)
            return MuxingResult.MuxingError("Muxing failed", Exception(e))
        }
    }
}
