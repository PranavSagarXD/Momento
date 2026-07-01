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
package com.sagar.momento.core.data_classes

import android.media.MediaFormat
import androidx.compose.ui.graphics.Color
import com.sagar.momento.core.enums.DateStyle
import com.sagar.momento.core.enums.Fonts
import com.sagar.momento.core.enums.VideoQuality

data class MontageConfig(
    val mimeType: String = MediaFormat.MIMETYPE_VIDEO_AVC,
    val bitrate: Int = 10_000_000,
    val iFrameInterval: Int = 1,

    // configurable
    val framesPerImage: Int = 1,
    val framesPerSecond: Float = 1f,
    val videoQuality: VideoQuality = VideoQuality.SMALL,
    val backgroundColor: Color = Color.Black,
    val waterMark: Boolean = true,
    val showDate: Boolean = true,
    val showMessage: Boolean = true,
    val font: Fonts = Fonts.FIGTREE,
    val dateStyle: DateStyle = DateStyle.FULL,
    val stabilizeFaces: Boolean = false,
    val censorFaces: Boolean = false,
)
