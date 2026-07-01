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

import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMuxer
import android.util.Log
import java.nio.ByteBuffer
import java.util.concurrent.TimeUnit

internal class Mp4FrameMuxer(path: String, fps: Float) : FrameMuxer {

    companion object {
        private const val TAG = "Mp4FrameMuxer"
    }

    private val frameUsec: Long = (TimeUnit.SECONDS.toMicros(1L) / fps).toLong()
    private val muxer = MediaMuxer(path, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)

    private var started = false
    private var videoTrackIndex: Int = -1
    private var audioTrackIndex: Int = -1
    private var videoFrames: Long = 0
    private var finalVideoTime: Long = 0

    override fun isStarted(): Boolean = started

    override fun start(videoFormat: MediaFormat, audioExtractor: MediaExtractor?) {
        audioExtractor?.let {
            try {
                it.selectTrack(0)
                val audioFormat = it.getTrackFormat(0)
                audioTrackIndex = muxer.addTrack(audioFormat)
                Log.d(TAG, "Added audio track: $audioFormat")
            } catch (e: Exception) {
                Log.w(TAG, "Failed to add audio track: ${e.message}")
            }
        }

        videoTrackIndex = muxer.addTrack(videoFormat)
        Log.d(TAG, "Added video track: $videoFormat")

        muxer.start()
        started = true
    }

    override fun muxVideoFrame(encodedData: ByteBuffer, bufferInfo: MediaCodec.BufferInfo) {
        if (!started) throw IllegalStateException("Muxer not started")

        finalVideoTime = frameUsec * videoFrames++
        bufferInfo.presentationTimeUs = finalVideoTime

        encodedData.position(bufferInfo.offset)
        encodedData.limit(bufferInfo.offset + bufferInfo.size)

        muxer.writeSampleData(videoTrackIndex, encodedData, bufferInfo)
    }

    override fun muxAudioFrame(encodedData: ByteBuffer, audioBufferInfo: MediaCodec.BufferInfo) {
        if (audioTrackIndex < 0) return
        encodedData.position(audioBufferInfo.offset)
        encodedData.limit(audioBufferInfo.offset + audioBufferInfo.size)
        muxer.writeSampleData(audioTrackIndex, encodedData, audioBufferInfo)
    }

    override fun release() {
        try {
            if (started) {
                muxer.stop()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error stopping muxer: ${e.message}")
        } finally {
            try {
                muxer.release()
            } catch (e: Exception) {
                Log.w(TAG, "Error releasing muxer: ${e.message}")
            }
            started = false
        }
    }

    override fun getVideoTime(): Long = finalVideoTime
}
